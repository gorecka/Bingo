package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.OfferStatus;
import communicationConstants.OntologyNames;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessUpdateOfferStatus extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;
    int offerID;
    //TODO: zmiana statusu oferty - do odebrania + potwierdzenie do wystawiającego
    public ProcessUpdateOfferStatus(AdvertisingColumnAgent advertisingColumn, ACLMessage message) {
        this.advertisingColumn = advertisingColumn;
        this.message = message;
        JSONObject json = new JSONObject(message.getContent());
        this.offerID = json.getInt("offerID");
    }

    @Override
    public void action() {

        System.out.println(advertisingColumn.getAID().getName() + " aktualizuję informacje o ustalonym odbiorze przedmiotu oferty - zaraz wyślę potwierdzenie do wystawiającego:\n"+message.getContent());

        //zmiana statusu oferty
        Offer offerToUpdate = advertisingColumn.getOfferById(offerID);
        offerToUpdate.setOfferStatus(OfferStatus.CLOSED);

        //wyslanie potwierdzenia aktualizacji statusu oferty

        ACLMessage confirmation;
        String content;
        JSONObject conf = new JSONObject();
        conf.put("message", "Status updated");
        content = conf.toString();

        confirmation = new ACLMessage(ACLMessage.AGREE);
        confirmation.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        confirmation.setContent(content);
        confirmation.addReceiver(message.getSender());

        advertisingColumn.send(confirmation);
    }
}
