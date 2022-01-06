package giver;

import advertisingColumn.WaitForFilledInReview;
import jade.core.Agent;

public class GiverAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
//        addBehaviour(new SendOffer(this));
        addBehaviour(new RequestMyPublishedOffers(this));
        addBehaviour(new WaitForReview(this));
        addBehaviour(new WaitForResignation(this));
//        addBehaviour(new RequestPossibleReceivers(this));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}
