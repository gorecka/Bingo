package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendApproval extends OneShotBehaviour {

    ReceiverAgent receiver;
    AID sender;

    public SendApproval(ReceiverAgent agent, AID sender, boolean b) {
        receiver = agent;
        this.sender = sender;
    }

    @Override
    public void action() {
        System.out.println("Agent " + receiver.getAID().getName() + " wysyłam potwierdzenie terminu");
        // przygotowanie wiadomości
        ACLMessage message;
        String content = "zgoda";

        message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(sender);

        receiver.send(message);
    }
}
