package giver;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestPossibleReceivers extends OneShotBehaviour {
    GiverAgent giver;

    RequestPossibleReceivers(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {

        System.out.println(giver.getAID().getName() + " zaraz wysle prosbe o listę chętnych");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology("Getting-possible-receivers-ontology");
        msg.setContent("Prośba o listę chętnych zgłoszonych do oferty nr 1");
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForListOfPossibleReceivers(giver));
    }
}
