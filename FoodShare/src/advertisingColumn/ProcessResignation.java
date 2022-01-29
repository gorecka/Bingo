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

        ACLMessage confirmation;
        JSONObject conf = new JSONObject();
        conf.put(JsonKeys.OFFER_ID, offerId);
        //sprawdzenie czy oferta istnieje
        if (offer == null) {
            // REFUSE - nie znaleziono oferty
            conf.put(JsonKeys.MESSAGE, "Offer with the given id was not found");
            confirmation = new ACLMessage(ACLMessage.REFUSE);
        } else {

            //sprawdzenie, czy rezygnujacy zostal wybrany do oferty
            boolean isChose = offer.getChosenReceiver().getUsername().equals(receiver.getLocalName());

            //znaleznienie wystawiajacego
            AID giver = new AID(offer.getAuthor().getUsername(), AID.ISLOCALNAME);


            if (isChose) {
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
            conf.put(JsonKeys.MESSAGE, "Deleted from possible receivers list");
            conf.put(JsonKeys.OFFER_ID, json.getInt(JsonKeys.OFFER_ID));

            confirmation = new ACLMessage(ACLMessage.AGREE);
            confirmation.setOntology(OntologyNames.RESIGNATION_ONTOLOGY);

        }
        String content;
        content = conf.toString();
        confirmation.setContent(content);
        confirmation.addReceiver(message.getSender());
        advertisingColumn.send(confirmation);
    }
}
