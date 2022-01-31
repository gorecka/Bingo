package advertisingColumn;

import advertisingColumn.data.Offer;
import communicationConstants.JsonKeys;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GetMyPublishedOffers extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    GetMyPublishedOffers(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // przetworzenie otrzymanej wiadomości
        String ontology = message.getOntology();
        AID giverAgent = message.getSender();

        ACLMessage reply;
        String replyContent;

        // pobranie listy ofert dodanych przez wstawiającego, od którego przyszła wiadomość
        List<Offer> myOffers = advertisingColumn.getOffersByAuthor(giverAgent.getLocalName());
        if (myOffers == null) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - wystąpił błąd przy pobieraniu listy ofert");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy ofert";
        } else {
            JSONObject myOffersJson = new JSONObject();
            JSONArray myPublishedOffers = new JSONArray();
            for (Offer offer : myOffers) {
                myPublishedOffers.put(offer.toJSON());
            }
            myOffersJson.put(JsonKeys.MY_PUBLISHED_OFFERS_LIST, myPublishedOffers);

            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - pobrano listę ofert");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = myOffersJson.toString();
        }
        reply.addReceiver(giverAgent);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }

}
