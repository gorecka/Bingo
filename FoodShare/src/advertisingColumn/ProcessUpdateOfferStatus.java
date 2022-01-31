package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.OfferStatus;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProcessUpdateOfferStatus extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;
    //TODO: zmiana statusu oferty - do odebrania + potwierdzenie do wystawiającego
    public ProcessUpdateOfferStatus(AdvertisingColumnAgent advertisingColumn, ACLMessage message) {
        this.advertisingColumn = advertisingColumn;
        this.message = message;
    }

    @Override
    public void action() {

        System.out.println(advertisingColumn.getAID().getName() + " aktualizuję informacje o ustalonym odbiorze przedmiotu oferty - zaraz wyślę potwierdzenie do wystawiającego:\n"+message.getContent());

        // aktualizacja oferty oferty
        JSONObject json = new JSONObject(message.getContent());
        int offerID = json.getInt(JsonKeys.OFFER_ID);
        Offer offerToUpdate = advertisingColumn.getOfferById(offerID);

        String receiverName = json.getString(JsonKeys.OFFER_CHOSEN_RECEIVER);
        User receiver = advertisingColumn.getUserByName(receiverName);

        String receiptPlace = json.getString(JsonKeys.OFFER_RECEIPT_PLACE);
        String receiptDate = json.getString(JsonKeys.OFFER_RECEIPT_DATE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
        Date recDate = null;
        try {
            recDate = formatter.parse(receiptDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        offerToUpdate.setOfferStatus(OfferStatus.CLOSED);
        offerToUpdate.setChosenReceiver(receiver);
        offerToUpdate.setReceiptDate(recDate);
        offerToUpdate.setReceiptPlace(receiptPlace);
        offerToUpdate.setReceiptDetailsSettled(new Date());

        //wyslanie potwierdzenia aktualizacji statusu oferty

        ACLMessage confirmation;
        String content;
        JSONObject conf = new JSONObject();
        conf.put(JsonKeys.MESSAGE, "Status updated");
        content = conf.toString();

        confirmation = new ACLMessage(ACLMessage.AGREE);
        confirmation.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        confirmation.setContent(content);
        confirmation.addReceiver(message.getSender());

        advertisingColumn.send(confirmation);
    }
}
