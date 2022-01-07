package advertisingColumn;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;
import receiver.ReceiverAgent;

public class CloseOffer extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    CloseOffer(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {

        // pobranie listy chętnych, którzy zgłosili się do oferty
        // TODO
        // SELECT * FROM RECEIVER WHERE ID != String chosenReceiver = message.getSender().getName();
        String[] possibleReceivers = {"rec1", "rec3", "rec5"};


            // przygotowanie wiadomości
        ACLMessage notifyReceivers;
        String notification;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject(message.getContent());

        obj.put("offerID", json.getString("offerID"));
        notification = obj.toString();
        notifyReceivers = new ACLMessage(ACLMessage.INFORM);
        notifyReceivers.setContent(notification);

        for (String possibleReceiver : possibleReceivers) {
            notifyReceivers.addReceiver(new AID(possibleReceiver, AID.ISLOCALNAME));
        }

        notifyReceivers.setOntology(OntologyNames.CONFIRMATION_OF_RECEIPT);
        advertisingColumn.send(notifyReceivers);

        System.out.println(advertisingColumn.getAID().getName() + " informuje o zakonczeniu oferty");

    }
}
