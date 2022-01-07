package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendProposalResponse extends OneShotBehaviour {

    ReceiverAgent receiver;
    AID giverSender;
    boolean ifProposalAccepted;

    public SendProposalResponse(ReceiverAgent agent, AID giverAgent, boolean b) {
        receiver = agent;
        this.giverSender = giverAgent;
        ifProposalAccepted = b;
    }

    @Override
    public void action() {
        // przygotowanie wiadomości
        ACLMessage message;
        String content;

        if (ifProposalAccepted) {
            message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            content = "zgoda";
            System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ZGODA");
            receiver.addBehaviour(new WaitForReviewForm(receiver));
        }
        else {
            message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
            content = "odmowa";
            System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ODMOWA");
        }

        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(giverSender);
        receiver.send(message);

    }
}
