package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class PublishOffer extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    PublishOffer(AdvertisingColumnAgent agent, ACLMessage msg) {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // przetworzenie otrzymanej wiadomości
        String ontology = message.getOntology();
        AID giverAgentName = message.getSender();
        System.out.println("Treść wiadomości: " + message.getContent());

        // sprawdzanie czy wystawiający nie ma zablokowanego konta
        // TODO
        JSONObject publishedOffer = new JSONObject();
        publishedOffer.put("offerID", "1234");
        String replyContent = publishedOffer.toString();

        boolean isAccountSuspended = false;

        ACLMessage reply;
        if (isAccountSuspended) {
            // nie można dodać oferty - przygotowanie wiadomości REFUSE
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - nie mozna dodac oferty");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Nie można dodać oferty - konto zablokowane";

        } else {
            // można dodać ofertę - dodanie oferty i przygotowanie wiadomości AGREE
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - oferta zostala dodana");
            reply = new ACLMessage(ACLMessage.AGREE);
        }

        reply.addReceiver(giverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }
}
