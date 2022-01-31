package advertisingColumn;

import advertisingColumn.data.Offer;
import communicationConstants.JsonKeys;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GetAllPublishedOffers extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    GetAllPublishedOffers(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // przetworzenie otrzymanej wiadomości
        String ontology = message.getOntology();
        AID receiverAgentName = message.getSender();

        ACLMessage reply;
        String replyContent;

        // pobranie listy aktualnych ofert dodanych przez wszystkich wstawiających
        // TODO: tworzenie listy json'ów można przenieść do metody getActiveOffers w advertisingColumn
        List<Offer> listOfAllPublishedOffers = advertisingColumn.getActiveOffers();
        if (listOfAllPublishedOffers == null) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do odbierajacego - wystąpił błąd przy pobieraniu listy ofert");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy ofert";
        } else {
            JSONObject allOffers = new JSONObject();
            JSONArray publishedOffers = new JSONArray();
            for (Offer o : listOfAllPublishedOffers) {
                publishedOffers.put(o.toJSON());
            }
            allOffers.put(JsonKeys.ALL_PUBLISHED_OFFERS_LIST, publishedOffers);

            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do odbierajacego - pobrano listę ofert");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = allOffers.toString();
        }
        reply.addReceiver(receiverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }

}
