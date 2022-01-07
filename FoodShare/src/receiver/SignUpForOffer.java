package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;


public class SignUpForOffer extends OneShotBehaviour {
    ReceiverAgent receiver;
    String offerID;

    SignUpForOffer(ReceiverAgent agent, String ID) {
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

        message = new ACLMessage(ACLMessage.REQUEST);
        message.setOntology(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        System.out.println(receiver.getAID().getName() + " zaraz zapisze sie na oferte do slupa");

        receiver.send(message);

        receiver.addBehaviour(new WaitForAddition(receiver));
    }
}