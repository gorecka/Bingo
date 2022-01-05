package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

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

        // pobranie listy chętnych, którzy zgłosili się do oferty
        // TODO
        String possibleReceivers = "user1, user2, user4";
        boolean isOperationSuccessful = true;

        ACLMessage reply;
        String replyContent;
        if (isOperationSuccessful) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - pobrano listę chętnych");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = possibleReceivers;
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
