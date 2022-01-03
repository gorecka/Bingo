package giver;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestPublishedOffers extends OneShotBehaviour {
    GiverAgent giver;

    RequestPublishedOffers(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {

        System.out.println(giver.getAID().getName() + " zaraz wysle prosbe o liste opublikowanych ofert");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology("Getting-published-offers-ontology");
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForListOfPublishedOffers(giver));
    }
}
