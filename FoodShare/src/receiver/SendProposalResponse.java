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
        String content;

        switch (decision) {
            case OK:
                message = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                content = "zgoda";
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ZGODA");
                receiver.addBehaviour(new WaitForReviewForm(receiver));
                break;
            case RESIGN:
                message = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                content = "odmowa";
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - ODMOWA");
                break;
            case CFP:
                message = new ACLMessage(ACLMessage.CFP);
                content = "prosba o nowy termin";
                System.out.println("Agent " + receiver.getAID().getName() + " wysyłam odpowiedź na propozycje terminu - INNY TERMIN");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + decision);
        }

        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(giverSender);
        receiver.send(message);
    }
}
