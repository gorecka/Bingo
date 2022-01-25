package advertisingColumn;

import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import jade.core.AID;
import jade.core.Agent;

import java.util.List;

public class AdvertisingColumnAgent extends Agent {

    private List<User> users;
    private List<Offer> offers;

    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForRequest(this));
        addBehaviour(new SendReviewForm(this, new AID("R1", AID.ISLOCALNAME), new AID("W1", AID.ISLOCALNAME).getName(), "Example offer"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}