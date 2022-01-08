package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

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

        // pobranie listy aktualnych ofert dodanych przez wszystkich wstawiających
        // TODO
        JSONObject allOffers = new JSONObject();
        JSONArray publishedOffers = new JSONArray();

        JSONObject offer_1 = new JSONObject();
        offer_1.put("product", "ser");
        offer_1.put("state", "nieotwarty");
        offer_1.put("bestBeforeDate", "02.02.2022");
        offer_1.put("description", "Przykładowy opis");

        JSONObject offer_2 = new JSONObject();
        offer_2.put("product", "bułka");
        offer_2.put("state", "nieotwarty");
        offer_2.put("bestBeforeDate", "25.01.2022");
        offer_2.put("description", "Przykładowy opis");

        publishedOffers.put(offer_1);
        publishedOffers.put(offer_2);

        allOffers.put("AllPublishedOffers", publishedOffers);

        boolean isOperationSuccessful = true;

        ACLMessage reply;
        String replyContent;
        if (isOperationSuccessful) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do odbierajacego - pobrano listę ofert");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = allOffers.toString();
        } else {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do odbierajacego - wystąpił błąd przy pobieraniu listy ofert");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy ofert";
        }
        reply.addReceiver(receiverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }

}
