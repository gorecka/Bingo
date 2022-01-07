package receiver;

import jade.core.Agent;

public class ReceiverAgent extends Agent {
    protected void setup() {

        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new RequestAllPublishedOffers(this));
        addBehaviour(new SignUpForOffer(this, "1"));
//        addBehaviour(new WaitForReviewForm(this));
//        addBehaviour(new ResignFromOffer(this , "12344"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}

