package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPossibleReceivers extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    GetPossibleReceivers(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // przetworzenie otrzymanej wiadomości
        String ontology = message.getOntology();
        AID giverAgentName = message.getSender();
        JSONObject content = new JSONObject(message.getContent()); // w treści wiadomości informacja o tym której oferty dotyczy wiadomość
        System.out.println("Wiadomość otrzymana od wystawiajacego: " + content);
        int offerId = content.getInt(JsonKeys.OFFER_ID);
        Offer offer = advertisingColumn.getOfferById(offerId);

        ACLMessage reply;
        String replyContent;

        if (offer == null) {
            // REFUSE - wystąpił błąd, nie znaleziono oferty o podanym Id
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - wystąpił błąd przy pobieraniu listy chętnych, nie znaleziono oferty z podanym id");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy chętnych - nie znaleziono oferty z podanym id";
        } else {
            // sprawdzenie, czy nadawca wiadomości jest autorem ogłoszenia
            String offerAuthor = offer.getAuthor().getUsername();
            boolean isAuthor = offerAuthor.equals(giverAgentName.getLocalName());
            if (!isAuthor) {
                // REFUSE - nadawca nie może otrzymać informacji o zgłoszonych do oferty, bo nie jest jej autorem
                System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - wystąpił błąd przy pobieraniu listy chętnych, nadawca nie jest autorem oferty");
                reply = new ACLMessage(ACLMessage.REFUSE);
                replyContent = "Wystąpił błąd przy pobieraniu listy chętnych - nadawca nie jest autorem oferty";

            } else {
                // pobranie listy chętnych, którzy zgłosili się do oferty
                List<User> possibleReceivers = offer.getPossibleReceivers();
                JSONObject replyJson = new JSONObject();
                JSONArray possibleReceiversArray = new JSONArray();

                for (User receiver : possibleReceivers) {
                    String receiverName = receiver.getUsername();
                    possibleReceiversArray.put(receiverName);
                }
                replyJson.put(JsonKeys.OFFER_ID, offerId);
                replyJson.put(JsonKeys.POSSIBLE_RECEIVERS_LIST, possibleReceiversArray);

                System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - pobrano listę chętnych");
                reply = new ACLMessage(ACLMessage.INFORM);
                replyContent = replyJson.toString();
            }
        }
        reply.addReceiver(giverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }
}
