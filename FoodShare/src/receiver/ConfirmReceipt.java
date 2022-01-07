package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;


public class ConfirmReceipt extends OneShotBehaviour {
    ReceiverAgent receiver;
    String offerID;

    ConfirmReceipt(ReceiverAgent agent, String ID) {
        receiver = agent;
        offerID = ID;
    }

    @Override
    public void action() {
        // przygotowanie wiadomości
        ACLMessage message;
        String content;
        JSONObject json = new JSONObject();
        json.put("offerID", offerID);
        content = json.toString();

        message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology(OntologyNames.CONFIRMATION_OF_RECEIPT);
        message.setContent(content);
        message.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        System.out.println(receiver.getAID().getName() + " informuje o odbiorze przesylki");

        receiver.send(message);
    }
}