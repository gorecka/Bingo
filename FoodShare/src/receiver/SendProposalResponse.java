package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendProposalResponse extends OneShotBehaviour {

    ReceiverAgent receiver;
    AID giverSender;
    WaitForProposal.ReceiverDecision decision;

    public SendProposalResponse(ReceiverAgent agent, AID giverAgent, WaitForProposal.ReceiverDecision dec) {
        receiver = agent;
        this.giverSender = giverAgent;
        decision = dec;
    }

    @Override
    public void action() {
        // przygotowanie wiadomości
        ACLMessage message;

        switch (decision) {
            case OK:
                message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ZGODA");
                receiver.addBehaviour(new WaitForReviewForm(receiver));
                break;
            case RESIGN:
                message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ODMOWA");
                break;
            case CFP:
                message = new ACLMessage(ACLMessage.CFP);
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - INNY TERMIN");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + decision);
        }

        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.addReceiver(giverSender);
        receiver.send(message);
    }
}
