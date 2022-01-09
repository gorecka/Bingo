package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONArray;
import org.json.JSONObject;

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
        String content = message.getContent(); // w treści wiadomości informacja o tym której oferty dotyczy wiadomość
        System.out.println("Wiadomość otrzymana od wystawiajacego: " + content);

        // pobranie listy chętnych, którzy zgłosili się do oferty
        // TODO
        JSONObject possibleReceivers = new JSONObject();
        JSONArray possibleReceiversArray = new JSONArray();

        possibleReceiversArray.put("user1");
        possibleReceiversArray.put("user2");
        possibleReceiversArray.put("user3");

        possibleReceivers.put("possibleReceivers", possibleReceiversArray);

        boolean isOperationSuccessful = true;

        ACLMessage reply;
        String replyContent;
        if (isOperationSuccessful) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - pobrano listę chętnych");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = possibleReceivers.toString();
        } else {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - wystąpił błąd przy pobieraniu listy chętnych");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Wystąpił błąd przy pobieraniu listy ofert";
        }
        reply.addReceiver(giverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }
}
