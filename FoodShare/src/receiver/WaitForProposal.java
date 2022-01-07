package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForProposal extends CyclicBehaviour {
    ReceiverAgent receiver;

    WaitForProposal(ReceiverAgent agent) {
        receiver = agent;
    }
    boolean proposalAccepted = true;
    boolean ifResignation = true;

    @Override
    public void action() {

        System.out.println("Agent " + receiver.getAID().getName() + " czekam na propozycje terminu");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);
        ACLMessage message = receiver.receive(mt);

        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal propozycje terminu ");
            message.getSender();
            receiver.addBehaviour(new SendProposalResponse(receiver, message.getSender(), true));
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal propozycji terminu - blokada");
            block();
        }
    }
}