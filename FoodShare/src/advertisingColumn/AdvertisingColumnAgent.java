package advertisingColumn;

import jade.core.AID;
import jade.core.Agent;

public class AdvertisingColumnAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForRequest(this));
        addBehaviour(new SendReviewForm(this, new AID("R1", AID.ISLOCALNAME), new AID("W1", AID.ISLOCALNAME).getName(), "Example offer"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}