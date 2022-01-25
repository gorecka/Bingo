package advertisingColumn;

import advertisingColumn.data.Offer;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendReviewForm extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    AID receiverName;
    String giverName;
    String offerName;
    int offerId;

    SendReviewForm(AdvertisingColumnAgent agent, int id){
        advertisingColumn = agent;
        offerId = id;
        Offer offer = advertisingColumn.getOfferById(offerId);
        receiverName = new AID(offer.getChosenReceiver().getUsername(), AID.ISLOCALNAME);
        giverName = offer.getAuthor().getUsername();
        offerName = offer.getName();
    }

    @Override
    public void action() {
        String content;
        JSONObject json = new JSONObject();
        json.put("offer", offerName);
        json.put("review", "In scale from 1 to 5 how satisfied are you with received food?");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        json.put("timestamp", dtf.format(LocalDateTime.now()));
        json.put("giver", giverName);
        json.put("offerId", offerId);

        content = json.toString();

        System.out.println(advertisingColumn.getAID().getName() + " zaraz wysle ankietę do " + receiverName);
        // przygotowanie ankiety i wysłanie do obiorcy jedzenia
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiverName);
        msg.setOntology(OntologyNames.REVIEWING_FORM_ONTOLOGY);
        msg.setContent(content);
        advertisingColumn.send(msg);

        // oczekiwanie na odpowiedź
        advertisingColumn.addBehaviour(new WaitForFilledInReview(advertisingColumn));
    }
}


