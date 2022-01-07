package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

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
        String publishedOffers = "Wszystkie oferty: 1. ser, 2. bułka";
        boolean isOperationSuccessful = true;

        ACLMessage reply;
        String replyContent;
        if (isOperationSuccessful) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do odbierajacego - pobrano listę ofert");
            reply = new ACLMessage(ACLMessage.INFORM);
            replyContent = publishedOffers;
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
