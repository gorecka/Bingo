package giver;

import advertisingColumn.data.ItemStatus;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class EditOffer extends OneShotBehaviour {
    GiverAgent giver;
    String offerId;

    EditOffer(GiverAgent agent, String offerID){
        giver = agent;
        offerId = offerID;
    }

    @Override
    public void action() {
        JSONObject newOffer = prepareOffer();

        System.out.println(giver.getAID().getName() + " sending EditOffer request");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.EDITING_OFFER_ONTOLOGY);
        msg.setContent(newOffer.toString());
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForEditOffer(giver));
    }

    JSONObject prepareOffer() {
        JSONObject offer = new JSONObject();
        offer.put(JsonKeys.OFFER_ID, offerId);
        offer.put(JsonKeys.OFFER_NAME, "ser");
        offer.put(JsonKeys.OFFER_ITEM_STATUS, ItemStatus.CLOSE_EXPIRATION);
        offer.put(JsonKeys.OFFER_BEST_BEFORE_DATE, "02-02-2022");
        offer.put(JsonKeys.OFFER_DESCRIPTION, "dobry ser");
        return offer;
    }
}