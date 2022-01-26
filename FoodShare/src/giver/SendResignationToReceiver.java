package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendResignationToReceiver extends OneShotBehaviour {

    GiverAgent giver;
    AID receiverAID;
    int  offerID;

    public SendResignationToReceiver(GiverAgent giver, AID receiver, int offerID) {
        this.giver = giver;
        this.receiverAID = receiver;
        this.offerID = offerID;
    }

    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " wysyłam informację o rezygnacji z negocjacji do agenta " + receiverAID.getName());
        // przygotowanie wiadomości
        ACLMessage message;
        JSONObject content = new JSONObject();
        content.put("offerID", offerID);
        content.put("id wystawiającego", giver.getAID());

        message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content.toString());
        message.addReceiver(receiverAID);
        giver.send(message);
    }
}
