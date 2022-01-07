package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendRequestForRemovingReceiver extends Behaviour {
    String offerID;
    AID receiverID;
    GiverAgent giver;
    public SendRequestForRemovingReceiver(GiverAgent giver, AID sender, String offerID) {
        this.giver = giver;
        this.receiverID = sender;
        this.offerID = offerID;

    }

    @Override
    public void action() {
        // przygotowanie wiadomości
        ACLMessage message;
        String content;
        JSONObject json = new JSONObject();
        json.put("offerID", offerID);
        json.put("receiverID", offerID);
        content = json.toString();

        message = new ACLMessage(ACLMessage.REQUEST);
        message.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        System.out.println(giver.getAID().getName() + " zaraz wysle rezygnacje odbierającego z mojej oferty do slupa");

        giver.send(message);

        giver.addBehaviour(new WaitForReceiverResignationConfirmation(giver));
    }

    @Override
    public boolean done() {
        return false;
    }
}
