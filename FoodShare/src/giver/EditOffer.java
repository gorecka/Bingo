package giver;

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
        // TODO: przygotowanie edytowanej oferty
        JSONObject content = new JSONObject();
        content.put("id", offerId);
        content.put("product", "ser");
        content.put("state", "nieotwarty");
        content.put("bestBeforeDate", "02.02.2022");

        System.out.println(giver.getAID().getName() + " sending EditOffer request");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.EDITING_OFFER_ONTOLOGY);
        msg.setContent(content.toString());
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForEditOffer(giver));
    }
}
