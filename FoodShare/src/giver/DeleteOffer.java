package giver;

import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class DeleteOffer extends OneShotBehaviour {
    GiverAgent giver;
    String offerId;

    DeleteOffer(GiverAgent agent, String offerID){
        giver = agent;
        offerId = offerID;
    }

    @Override
    public void action() {
        JSONObject content = new JSONObject();
        content.put(JsonKeys.OFFER_ID, offerId);

        System.out.println(giver.getAID().getName() + " sending DeleteOffer request");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.DELETING_OFFER_ONTOLOGY);
        msg.setContent(content.toString());
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForDeleteOffer(giver));
    }
}
