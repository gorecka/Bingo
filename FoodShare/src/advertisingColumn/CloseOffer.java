package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;
import receiver.ReceiverAgent;

import java.util.List;

public class CloseOffer extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    CloseOffer(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        System.out.println(advertisingColumn.getAID().getName() + " dostal prosbe o zamkniecie oferty od " + message.getSender().getLocalName());

        AID receiver = message.getSender();
        JSONObject content = new JSONObject(message.getContent());
        int offerId = content.getInt(JsonKeys.OFFER_ID);

        // przygotowanie wiadomości
        ACLMessage response;
        String responseString;
        JSONObject obj = new JSONObject();
        obj.put(JsonKeys.OFFER_ID, offerId);

        Offer offer = advertisingColumn.getOfferById(offerId);
        if (offer == null) {
            // REFUSE - nie znaleziono oferty, odpowiedź tylko to 1 odbiorcy, ktory wysłał wiadomość
            response = new ACLMessage(ACLMessage.REFUSE);
            obj.put(JsonKeys.MESSAGE, "Offer with the given id was not found");
            response.addReceiver(new AID(receiver.getLocalName(), AID.ISLOCALNAME));
        } else {
            String chosenReceiverName = offer.getChosenReceiver().getUsername();
            if (chosenReceiverName.equals(receiver.getLocalName())) {
                // INFORM - zmiana statusu oferty i ustawienie daty potwierdzenia odbioru, odpowiedź do wszystkich odbiorców z listy chętnych
                offer.closeOffer();
                response = new ACLMessage(ACLMessage.INFORM);
                obj.put(JsonKeys.MESSAGE, "Offer closed");
                // pobranie listy chętnych, którzy zgłosili się do oferty
                List<User> possibleReceivers = offer.getPossibleReceivers();
                for (User possibleReceiver : possibleReceivers) {
                    response.addReceiver(new AID(possibleReceiver.getUsername(), AID.ISLOCALNAME));
                }
            } else {
                // REFUSE - użytkownik który chce potwierdzić odbiór nie jest wybranym odbiorcą, odpowiedź tylko to 1 odbiorcy, ktory wysłał wiadomość
                response = new ACLMessage(ACLMessage.REFUSE);
                obj.put(JsonKeys.MESSAGE, "User who wants to confirm receipt is not the chosen recipient");
                response.addReceiver(new AID(receiver.getLocalName(), AID.ISLOCALNAME));
            }
        }

        responseString = obj.toString();
        response.setContent(responseString);
        response.setOntology(OntologyNames.CONFIRMATION_OF_RECEIPT);
        advertisingColumn.send(response);
    }
}