package receiver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;


public class ResignFromOffer extends OneShotBehaviour {
    ReceiverAgent receiver;
    String offerID;

    ResignFromOffer(ReceiverAgent agent, String ID) {
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
        message.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        System.out.println(receiver.getAID().getName() + " zaraz wysle rezygnacje z oferty do slupa");

        receiver.send(message);

        receiver.addBehaviour(new WaitForConfirmation(receiver));
    }
}