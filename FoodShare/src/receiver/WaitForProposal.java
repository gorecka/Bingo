package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForProposal extends CyclicBehaviour {
    ReceiverAgent receiver;
    enum ReceiverDecision {
        OK,
        RESIGN,
        CFP
    }

    WaitForProposal(ReceiverAgent agent) {
        receiver = agent;
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
        ACLMessage message = receiver.receive(mt);

        if (message != null) {
            // idoferty z treści
            JSONObject json = new JSONObject(message.getContent());
            int offerID = json.getInt("offerID");
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal propozycje terminu dla oferty o id=" + offerID + ": \n" + message.getContent() + "\n");
            message.getSender();

            //TODO: podjęcie decyzji dotyczącej terminu - wybór wartości zmiennej dec
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("\t\t wybierz OK, RESIGN lub CFP");
//            String choice = scanner.next();
            String choice = "CFP";
            switch(choice) {
                case "OK":
                    receiver.addBehaviour(new SendProposalResponse(receiver, message.getSender(), offerID, ReceiverDecision.OK));
                    break;
                case "RESIGN":
                    receiver.addBehaviour(new SendProposalResponse(receiver, message.getSender(), offerID, ReceiverDecision.RESIGN));
                    break;
                case "CFP":
                    receiver.addBehaviour(new SendProposalResponse(receiver, message.getSender(), offerID, ReceiverDecision.CFP));
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal propozycji terminu - blokada");
            block();
        }
    }
}