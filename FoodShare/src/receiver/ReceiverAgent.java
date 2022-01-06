package receiver;

import advertisingColumn.SendReviewForm;
import jade.core.Agent;

public class ReceiverAgent extends Agent {
    protected void setup() {

        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForReviewForm(this));
        addBehaviour(new ResignFromOffer(this , "12344"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}

