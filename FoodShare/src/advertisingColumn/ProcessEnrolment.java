package advertisingColumn;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessEnrolment extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessEnrolment(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // sprawdzenie, czy wybrany do oferty i kto wystawia
        //TODO
        boolean isChose = true;
        AID giver = new AID("W1", AID.ISLOCALNAME);

        JSONObject json = new JSONObject(message.getContent());
        if(isChose) {
            ACLMessage notifyGiver;
            String notification;
            JSONObject obj = new JSONObject();

            obj.put("receiver", message.getSender());
            obj.put("offerID", json.getString("offerID"));
            notification = obj.toString();
            notifyGiver = new ACLMessage(ACLMessage.INFORM);
            notifyGiver.setContent(notification);
            notifyGiver.addReceiver(giver);
            notifyGiver.setOntology(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY);
            advertisingColumn.send(notifyGiver);

        }
        //dodaj uzytkownika do listy chetnych
        // TODO


        //wyslanie potwierdzenia usuniecia z listy chetnych
        ACLMessage confirmation;
        String content;
        JSONObject conf = new JSONObject();
        conf.put("message", "Added to possible receivers list");
        conf.put("offerID", json.getString("offerID"));
        content = conf.toString();

        confirmation = new ACLMessage(ACLMessage.AGREE);
        confirmation.setOntology(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY);
        confirmation.setContent(content);
        confirmation.addReceiver(message.getSender());

        advertisingColumn.send(confirmation);
    }
}
