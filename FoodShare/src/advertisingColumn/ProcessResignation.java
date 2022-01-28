package advertisingColumn;

import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessResignation extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessResignation(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        JSONObject json = new JSONObject(message.getContent());
        AID receiver = new AID(json.getString(JsonKeys.OFFER_RECEIVER_NAME), AID.ISGUID);
        boolean isChose = true;
        AID giver = new AID("W1", AID.ISLOCALNAME);

        // sprawdzenie, czy wybrany do oferty i kto wystawia
        //TODO
        if(isChose) {
            ACLMessage notifyGiver;
            String notification;
            JSONObject obj = new JSONObject();

            obj.put(JsonKeys.OFFER_RECEIVER_NAME, receiver.getName());
            obj.put(JsonKeys.OFFER_ID, json.getInt(JsonKeys.OFFER_ID));
            notification = obj.toString();
            notifyGiver = new ACLMessage(ACLMessage.INFORM);
            notifyGiver.setContent(notification);
            notifyGiver.addReceiver(giver);
            notifyGiver.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);
            advertisingColumn.send(notifyGiver);

        }
        //usun uzytkownika z listy chetnych
        // TODO


        //wyslanie potwierdzenia usuniecia z listy chetnych
        ACLMessage confirmation;
        String content;
        JSONObject conf = new JSONObject();
        conf.put(JsonKeys.MESSAGE, "Deleted from possible receivers list");
        conf.put(JsonKeys.OFFER_ID, json.getInt(JsonKeys.OFFER_ID));
        content = conf.toString();

        confirmation = new ACLMessage(ACLMessage.AGREE);
        confirmation.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        confirmation.setContent(content);
        confirmation.addReceiver(message.getSender());

        advertisingColumn.send(confirmation);
    }
}
