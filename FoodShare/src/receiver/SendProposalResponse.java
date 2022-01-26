package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendProposalResponse extends OneShotBehaviour {

    ReceiverAgent receiver;
    AID giverSender;
    int offerID;
    WaitForProposal.ReceiverDecision decision;

    public SendProposalResponse(ReceiverAgent agent, AID giverAgent, int offerID, WaitForProposal.ReceiverDecision dec) {
        receiver = agent;
        this.giverSender = giverAgent;
        this.offerID = offerID;
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
        JSONObject content = new JSONObject();
        content.put("offerID", offerID);
        message.setContent(content.toString());
        message.addReceiver(giverSender);
        receiver.send(message);
    }
}
