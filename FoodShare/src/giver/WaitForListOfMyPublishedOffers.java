package giver;

import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class WaitForListOfMyPublishedOffers extends Behaviour {
    boolean isDone = false;
    GiverAgent giver;

    WaitForListOfMyPublishedOffers(GiverAgent agent) {
        giver = agent;
    }

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.GETTING_MY_PUBLISHED_OFFERS_ONTOLOGY);
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal wiadomość ");
            System.out.println("Treść wiadomości: " + message.getContent());
            int performative = message.getPerformative();
            if (performative == ACLMessage.INFORM) {
                System.out.println("Otrzymano listę ofert opublikowanych przez użytkownika " + giver.getLocalName() + "(lista może być pusta):");
                printReceivedOffers(message.getContent());
            } else if (performative == ACLMessage.REFUSE) {
                System.out.println("Wystąpił błąd w wyniku którego nie udało się pobrać listy opublikowanych ofert");
            }
            isDone = true;
        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }

    private void printReceivedOffers(String offersString) {
        JSONObject offersJson = new JSONObject(offersString);
        JSONArray offersArray = offersJson.getJSONArray(JsonKeys.MY_PUBLISHED_OFFERS_LIST);

        if(offersArray.isEmpty()){
            System.out.println("Brak opublikowanych ofert");
        }

        for (Object offer : offersArray) {
            JSONObject offerJson = (JSONObject) offer;
            System.out.println("------------- offerId: " + offerJson.getInt(JsonKeys.OFFER_ID) + "-------------");
            System.out.println("Name: " + offerJson.getString(JsonKeys.OFFER_NAME));
            System.out.println("Description: " + offerJson.getString(JsonKeys.OFFER_DESCRIPTION));
            System.out.println("Author: " + offerJson.getString(JsonKeys.OFFER_AUTHOR));
            System.out.println("Item status: " + offerJson.getString(JsonKeys.OFFER_ITEM_STATUS));
            System.out.println("Best before date: " + offerJson.getString(JsonKeys.OFFER_BEST_BEFORE_DATE));
            System.out.println("Date of publishing offer: " + offerJson.getString(JsonKeys.OFFER_CREATION_DATE));
        }
    }
}
