package advertisingColumn;

import advertisingColumn.data.User;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.text.ParseException;

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
        String content = message.getContent();
        System.out.println("Treść wiadomości: " + content);

        String replyContent;
        ACLMessage reply;

        // sprawdzanie czy wystawiający nie ma zablokowanego konta
        String username = giverAgentName.getLocalName();
        User giverUser = advertisingColumn.getUserByName(username);
        if (giverUser == null) {
            System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - nie znaleziono użytkownika który chciał dodać ofertę");
            reply = new ACLMessage(ACLMessage.REFUSE);
            replyContent = "Nie znaleziono użytkownika który chciał dodać ofertę";
        } else {
            boolean isAccountSuspended = giverUser.isSuspended();

            if (isAccountSuspended) {
                // nie można dodać oferty - przygotowanie wiadomości REFUSE
                System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - nie mozna dodac oferty");
                reply = new ACLMessage(ACLMessage.REFUSE);
                replyContent = "Nie można dodać oferty - konto zablokowane";
            } else {
                // można dodać ofertę - dodanie oferty i przygotowanie wiadomości AGREE
                int publishedOfferId = 0;
                try {
                    publishedOfferId = advertisingColumn.publishOffer(content, giverUser);
                } catch (ParseException e) {
                    System.out.println(advertisingColumn.getAID().getName() + " failed to parse bestBeforeDate");
                }
                JSONObject publishedOffer = new JSONObject();
                publishedOffer.put("offerID", publishedOfferId);
                replyContent = publishedOffer.toString();

                System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle wiadomosc do wystawiajacego - oferta zostala dodana");
                System.out.println("Opublikowana oferta: " + advertisingColumn.getOfferById(publishedOfferId));
                reply = new ACLMessage(ACLMessage.AGREE);
            }
        }

        reply.addReceiver(giverAgentName);
        reply.setOntology(ontology);
        reply.setContent(replyContent);
        advertisingColumn.send(reply);
    }
}
