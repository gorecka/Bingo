package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForProposalAnswear extends Behaviour {

    GiverAgent giver;
    boolean isDone = false;
    WaitForProposalAnswear(GiverAgent agent) {
        giver = agent;
    }
    //TODO ID oferty
    int offerID = 1;

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

        //TODO: szablon odpowiedzi na CFP - prośba o nowy termin
        ACLMessage messageAccept = giver.receive(mtAccept);
        ACLMessage messageReject = giver.receive(mtReject);
        ACLMessage messageCFP = giver.receive(mtCFP);

        boolean refuseCFP = true;

        if (messageAccept != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal akceptację terminu -> wysyłam informacje do słupa ");
            isDone = true;
            giver.addBehaviour(new SendCollectionDetails(giver, messageAccept.getSender(), offerID));
        } else if (messageReject != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal odmowę terminu - koniec negocjacji");
            isDone = true;
            giver.addBehaviour(new SendRequestForRemovingReceiver(giver, messageReject.getSender(), offerID));
        } else if (messageCFP != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal CFP - podejmuję decyzję czy kontyuować negocjacje");
            if (refuseCFP) {
                giver.addBehaviour(new SendRequestForRemovingReceiver(giver, messageCFP.getSender(), offerID));
                giver.addBehaviour(new SendResignationToReceiver(giver, messageCFP.getSender(), offerID));
            } else giver.addBehaviour(new SendProposal(giver, messageCFP.getSender()));
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
