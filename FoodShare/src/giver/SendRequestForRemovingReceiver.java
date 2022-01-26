package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendRequestForRemovingReceiver extends OneShotBehaviour {
    String receiver;
    int offerID;
    GiverAgent giver;

    public SendRequestForRemovingReceiver(GiverAgent giver, AID sender, int offerID) {
        this.giver = giver;
        this.receiver = sender.getName();
        this.offerID = offerID;
    }

    @Override
    public void action() {
        // przygotowanie wiadomości
        ACLMessage message;
        String content;
        JSONObject json = new JSONObject();
        json.put("offerID", offerID);
        json.put("receiverName", receiver);
        content = json.toString();

        message = new ACLMessage(ACLMessage.REQUEST);
        message.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        System.out.println(giver.getAID().getName() + " zaraz wysle rezygnacje odbierającego z mojej oferty do slupa, \ntreść wysyłanej wiadomości: \n" + content);

        giver.send(message);

        giver.addBehaviour(new WaitForReceiverResignationConfirmation(giver));
    }
}
