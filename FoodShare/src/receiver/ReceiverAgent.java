package receiver;

import jade.core.Agent;

public class ReceiverAgent extends Agent {
    protected void setup() {

        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForResult(this));
//        addBehaviour(new WaitForReviewForm(this));
//        addBehaviour(new WaitForProposal(this));

        addBehaviour(new RequestAllPublishedOffers(this)); // 10
        addBehaviour(new SignUpForOffer(this, "1357")); // 20
        addBehaviour(new ConfirmReceipt(this, "1357")); // 50


//        addBehaviour(new ResignFromOffer(this , "12344"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}

