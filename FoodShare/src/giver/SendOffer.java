package giver;

import advertisingColumn.data.ItemStatus;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendOffer extends OneShotBehaviour {
    GiverAgent giver;

    SendOffer(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {
        // przygotowanie oferty
        JSONObject content = new JSONObject();
        content.put(JsonKeys.OFFER_NAME, "ser");
        content.put(JsonKeys.OFFER_ITEM_STATUS, ItemStatus.FRESH);
        content.put(JsonKeys.OFFER_BEST_BEFORE_DATE, "02-02-2022");
        content.put(JsonKeys.OFFER_DESCRIPTION, "Ser Gouda");

        System.out.println(giver.getAID().getName() + " zaraz wysle wiadomosc do slupa");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.PUBLISHING_OFFER_ONTOLOGY);
        msg.setContent(content.toString());
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForPublishingOffer(giver));
    }
}
