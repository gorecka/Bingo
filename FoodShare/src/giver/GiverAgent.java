package giver;

import jade.core.Agent;

public class GiverAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new SendOffer(this));
//        addBehaviour(new RequestMyPublishedOffers(this));
        addBehaviour(new WaitForReview(this));
        addBehaviour(new WaitForResignation(this));
//        addBehaviour(new RequestPossibleReceivers(this));
//        addBehaviour(new EditOffer(this, "4321"));
//        addBehaviour(new DeleteOffer(this, "1234"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}
