package giver;

import jade.core.Agent;

public class GiverAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
//        addBehaviour(new SendOffer(this));
//        addBehaviour(new RequestPublishedOffers(this));
        addBehaviour(new RequestPossibleReceivers(this));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}
