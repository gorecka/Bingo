package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessReviewForm extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessReviewForm(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {

        System.out.println("Dostalem ankiete i zaczynam przetwarzac");
        String content = message.getContent();
        JSONObject obj = new JSONObject(content);
        AID giver = new AID(obj.getString(JsonKeys.OFFER_AUTHOR), AID.ISLOCALNAME);
        int score = Integer.parseInt(obj.getString(JsonKeys.OFFER_REVIEW));
        int offerId = obj.getInt(JsonKeys.OFFER_ID);
        Offer offer = advertisingColumn.getOfferById(offerId);

        String authorName = offer.getAuthor().getUsername();
        User author = advertisingColumn.getUserByName(authorName);
        //dodanie oceny na uzytkownika
        User updatedUser = advertisingColumn.addReviewForUser(author, score);
        double avg = (double) updatedUser.getRatingSum()/updatedUser.getRatingCount();

        boolean isBlocked = updatedUser.isSuspended();
        System.out.println("Wysle ankiete do " + giver);

        System.out.println("Ocena: " + avg + " Czy blokada? "  + isBlocked);
        //wyslanie informacji o nowej ocenie i potencjalnej blokadzie
        ACLMessage reply;
        String replyContent;
        JSONObject json = new JSONObject();
        json.put(JsonKeys.OFFER_REVIEW, Integer.toString(score));
        json.put(JsonKeys.OFFER_REVIEWER, message.getSender().getLocalName());
        json.put(JsonKeys.USER_RATING_AVERAGE, Double.toString(avg));
        json.put(JsonKeys.USER_IS_BLOCKED, Boolean.toString(isBlocked));
        replyContent = json.toString();

        reply = new ACLMessage(ACLMessage.INFORM);
        reply.setContent(replyContent);
        reply.setOntology(OntologyNames.REVIEWING_FORM_ONTOLOGY);
        reply.addReceiver(giver);
        System.out.println(reply.getAllReceiver().next().toString() + " moj odbiorca ankiety");
        advertisingColumn.send(reply);
    }

}
