package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

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
        AID giverAgentName = message.getSender();

        // pobranie listy ofert dodanych przez wstawiającego, od którego przyszła wiadomość
        // TODO
        JSONObject myOffers = new JSONObject();
        JSONArray myPublishedOffers = new JSONArray();

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

        myPublishedOffers.put(offer_1);
        myPublishedOffers.put(offer_2);

        myOffers.put("myPublishedOffers", myPublishedOffers);

        boolean isOperationSuccessful = true;

        ACLMessage reply;
        String replyContent;
        if (isOperationSuccessful) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - pobrano listę ofert");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = myOffers.toString();
        } else {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - wystąpił błąd przy pobieraniu listy ofert");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy ofert";
        }
        reply.addReceiver(giverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }

}
