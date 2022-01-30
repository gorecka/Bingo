package giver;

import jade.core.AID;
import jade.core.Agent;

public class GiverAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForReview(this));
        addBehaviour(new WaitForResignation(this));

        addBehaviour(new SendOffer(this)); // 0
        addBehaviour(new RequestPossibleReceivers(this)); // 30
        addBehaviour(new SendProposal(this, new AID("receiver1", AID.ISLOCALNAME), 1357)); // 40

//        addBehaviour(new RequestMyPublishedOffers(this));
//        addBehaviour(new EditOffer(this, "4321"));
//        addBehaviour(new DeleteOffer(this, "1234"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}
