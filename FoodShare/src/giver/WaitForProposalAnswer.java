package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForProposalAnswer extends Behaviour {

    GiverAgent giver;
    boolean isDone = false;
    WaitForProposalAnswer(GiverAgent agent) {
        giver = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " czekam na akceptację/odmowę zaproponowanego przeze mnie terminu");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformativeAccept = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        MessageTemplate mtPerformativeReject = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        MessageTemplate mtPerformativeCFP = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);

        MessageTemplate mtAccept = MessageTemplate.and(mtPerformativeAccept, mtOntology);
        MessageTemplate mtReject = MessageTemplate.and(mtPerformativeReject, mtOntology);
        MessageTemplate mtCFP = MessageTemplate.and(mtPerformativeCFP, mtOntology);

        ACLMessage messageAccept = giver.receive(mtAccept);
        ACLMessage messageReject = giver.receive(mtReject);
        ACLMessage messageCFP = giver.receive(mtCFP);

        boolean refuseCFP = true;

        if (messageAccept != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal akceptację terminu -> wysyłam informacje do słupa ");
            isDone = true;
            JSONObject json = new JSONObject(messageAccept.getContent());
            giver.addBehaviour(new SendCollectionDetails(giver, messageAccept.getSender(), json.getInt("offerID")));
        } else if (messageReject != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal odmowę terminu - koniec negocjacji");
            isDone = true;
            JSONObject json = new JSONObject(messageReject.getContent());
            giver.addBehaviour(new SendRequestForRemovingReceiver(giver, messageReject.getSender(), json.getInt("offerID")));
        } else if (messageCFP != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal CFP - podejmuję decyzję czy kontyuować negocjacje");
            JSONObject json = new JSONObject(messageCFP.getContent());
            if (refuseCFP) {
                System.out.println("Rezygnuję z odbierającego");
                giver.addBehaviour(new SendRequestForRemovingReceiver(giver, messageCFP.getSender(), json.getInt("offerID")));
                giver.addBehaviour(new SendResignationToReceiver(giver, messageCFP.getSender(), json.getInt("offerID")));
            } else {
                System.out.println("Proponuję nowy termin");
                giver.addBehaviour(new SendProposal(giver, messageCFP.getSender()));
            }
            isDone = true;
        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie otrzymał akceptacji terminu - blokada");
            block();
        }

    }

    @Override
    public boolean done() {
        return isDone;
    }
}
