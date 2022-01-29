package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessEnrolment extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessEnrolment(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        AID receiver = message.getSender();
        System.out.println(advertisingColumn.getAID().getName() + " ProcessEnrolment from " + receiver.getName());
        JSONObject content = new JSONObject(message.getContent());
        int offerId = content.getInt(JsonKeys.OFFER_ID);

        ACLMessage confirmation;
        JSONObject conf = new JSONObject();
        conf.put(JsonKeys.OFFER_ID, offerId);

        Offer offer = advertisingColumn.getOfferById(offerId);
        if (offer == null) {
            // REFUSE - nie znaleziono oferty
            conf.put(JsonKeys.MESSAGE, "Offer with the given id was not found");
            confirmation = new ACLMessage(ACLMessage.REFUSE);
        } else {
            String receiverName = receiver.getLocalName();
            boolean isReceiverAdded = offer.isReceiverAdded(receiverName);
            if (isReceiverAdded) {
                // REFUSE - użytkownk jest już na liście chętnych
                conf.put(JsonKeys.MESSAGE, "Receiver is already on possible receivers list");
                confirmation = new ACLMessage(ACLMessage.REFUSE);
            } else {
                User receiverUser = advertisingColumn.getUserByName(receiverName);
                if (receiverUser == null) {
                    // REFUSE - nie znaleziono użytkownika
                    conf.put(JsonKeys.MESSAGE, "User " + receiverName + " was not found");
                    confirmation = new ACLMessage(ACLMessage.REFUSE);
                } else {
                    // AGREE - dodanie użytkownika do listy chętnych
                    offer.addPossibleReceiver(receiverUser);
                    conf.put(JsonKeys.MESSAGE, "Added to possible receivers list");
                    confirmation = new ACLMessage(ACLMessage.AGREE);
                }
            }
        }

        //wyslanie potwierdzenia dodania do listy chetnych
        String replyContent = conf.toString();
        confirmation.setOntology(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY);
        confirmation.setContent(replyContent);
        confirmation.addReceiver(receiver);

        advertisingColumn.send(confirmation);
    }
}
