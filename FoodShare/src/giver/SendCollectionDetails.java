package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendCollectionDetails extends OneShotBehaviour {
    GiverAgent giver;
    AID receiverID;
    int offerID;
    public SendCollectionDetails(GiverAgent agent, AID receiverID, int offerID) {
        giver = agent;
        this.receiverID = receiverID;
        this.offerID = offerID;
    }

    @Override
    public void action() {
        System.out.println(giver.getAID().getName() + " zaraz wysle do słupa informację o ustalonych szczegółach odbioru");
        //String content = "miejsce X, godzina Y";

        JSONObject content = new JSONObject();
        content.put("offerID", 1);
        content.put("place", "miejsce A");
        content.put("date", "20.01.2022 05:00:00 PM");
        content.put("receiverID", receiverID.toString());

        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        msg.setContent(content.toString());
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForOfferUpdateConfirmation(giver));
    }
}
