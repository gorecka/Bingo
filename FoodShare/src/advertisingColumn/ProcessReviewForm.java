package advertisingColumn;

import advertisingColumn.data.Offer;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;

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
        AID giver = new AID(obj.getString("giver"));
        int score = Integer.parseInt(obj.getString("review"));
        int offerId = Integer.parseInt(obj.getString("offerId"));
        Offer offer = advertisingColumn.getOfferById(offerId);

        // pobranie obecnej oceny wystawiajacego
        offer.getAuthor().addRating(score);
        int ratingCount = offer.getAuthor().getRatingCount();
        double avg = (double) offer.getAuthor().getRatingSum() / ratingCount;
        if (ratingCount > 2 && avg < 2) {
            Date dt = new Date();
            offer.getAuthor().setSuspended(true);
            offer.getAuthor().setSuspensionStart(dt);
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 7);
            dt = c.getTime();
            offer.getAuthor().setSuspensionEnd(dt );
        }

        boolean isBlocked = offer.getAuthor().isSuspended();
        System.out.println("Wysle ankiete do " + giver);

        System.out.println("Ocena: " + avg + " Czy blokada? "  + isBlocked);
        //wyslanie informacji o nowej ocenie i potencjalnej blokadzie
        ACLMessage reply;
        String replyContent;
        JSONObject json = new JSONObject();
        json.put("review", Double.toString(score));
        json.put("reviewer", message.getSender());
        json.put("average", Double.toString(avg));
        json.put("isBlocked", Boolean.toString(isBlocked));
        replyContent = json.toString();

        reply = new ACLMessage(ACLMessage.INFORM);
        reply.setContent(replyContent);
        reply.setOntology(OntologyNames.REVIEWING_FORM_ONTOLOGY);
        reply.addReceiver(giver);
        System.out.println(reply.getAllReceiver().next().toString() + " moj odbiorca ankiety");
        advertisingColumn.send(reply);
    }

}
