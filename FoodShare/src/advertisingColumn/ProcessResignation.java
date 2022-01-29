package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.util.List;

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
        int offerId = Integer.parseInt(json.getString(JsonKeys.OFFER_ID));
        Offer offer = advertisingColumn.getOfferById(offerId);

        //sprawdzenie, czy rezygnujacy zostal wybrany do oferty
        boolean isChose = offer.getChosenReceiver().getUsername().equals(receiver.getLocalName());

        //znaleznienie wystawiajacego
        AID giver = new AID(offer.getAuthor().getUsername(), AID.ISLOCALNAME);



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
        offer.getPossibleReceivers().removeIf(user -> user.getUsername().equals(receiver.getLocalName()));
        advertisingColumn.updateOffer(offer);

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
