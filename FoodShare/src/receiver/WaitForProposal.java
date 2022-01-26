package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForProposal extends Behaviour {
    ReceiverAgent receiver;
    Boolean isDone;
    enum ReceiverDecision {
        OK,
        RESIGN,
        CFP
    }

    WaitForProposal(ReceiverAgent agent) {
        receiver = agent;
        isDone = false;
    }
    boolean proposalAccepted = true;
    boolean ifResignation = true;
    ReceiverDecision dec = ReceiverDecision.CFP;

    // Agent wysyła CFP 1 raz, następnie zgadza się na propozycję wystawiającego
    private int CFPCounter = 0;

    @Override
    public void action() {
        if(dec == ReceiverDecision.CFP && CFPCounter == 1) dec = ReceiverDecision.OK;

        System.out.println("Agent " + receiver.getAID().getName() + " czekam na propozycje terminu");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        MessageTemplate mtPerformativeReject = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtOntologyReject = MessageTemplate.MatchOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        MessageTemplate mtReject = MessageTemplate.and(mtPerformativeReject, mtOntologyReject);

        ACLMessage messageProposal = receiver.receive(mt);
        ACLMessage messageReject = receiver.receive(mtReject);


        if (messageProposal != null) {
            // idoferty z treści
            JSONObject json = new JSONObject(messageProposal.getContent());
            int offerID = json.getInt("offerID");
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal propozycje terminu dla oferty o id=" + offerID + ": \n" + messageProposal.getContent());
            messageProposal.getSender();

            //TODO: podjęcie decyzji dotyczącej terminu - wybór wartości zmiennej dec
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("\t\t wybierz OK, RESIGN lub CFP");
//            String choice = scanner.next();
            String choice = "RESIGN";
            switch(choice) {
                case "OK":
                    receiver.addBehaviour(new SendProposalResponse(receiver, messageProposal.getSender(), offerID, ReceiverDecision.OK));
                    isDone = true;
                    break;
                case "RESIGN":
                    receiver.addBehaviour(new SendProposalResponse(receiver, messageProposal.getSender(), offerID, ReceiverDecision.RESIGN));
                    isDone = true;
                    break;
                case "CFP":
                    receiver.addBehaviour(new SendProposalResponse(receiver, messageProposal.getSender(), offerID, ReceiverDecision.CFP));
                    isDone = true;
                    break;
                default:
                    break;
            }
        } else if (messageReject != null) {
            JSONObject json = new JSONObject(messageReject.getContent());
            int offerID = json.getInt("offerID");
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal odmowę wystawiającego id oferty =" + offerID + ": \n" + messageReject.getContent() + "\n");
            isDone = true;
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal propozycji terminu - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}