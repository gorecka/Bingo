package giver;

import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class RequestPossibleReceivers extends OneShotBehaviour {
    GiverAgent giver;

    RequestPossibleReceivers(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {

        JSONObject jsonContent = new JSONObject();
        jsonContent.put(JsonKeys.OFFER_ID, "2345");
        String content = jsonContent.toString();

        System.out.println(giver.getAID().getName() + " zaraz wysle prosbe o listę chętnych");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.GETTING_POSSIBLE_RECEIVERS_ONTOLOGY);
        msg.setContent(content);
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForListOfPossibleReceivers(giver));
    }
}
