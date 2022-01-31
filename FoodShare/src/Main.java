import jade.wrapper.AgentContainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

    public static void main(String[] args) {
        Profile profile = new ProfileImpl();
        AgentContainer container = Runtime.instance().createMainContainer(profile);

        try {
            AgentController giver1 = container.createNewAgent("W1", "giver.GiverAgent", null);
            AgentController receiver1 = container.createNewAgent("R1", "receiver.ReceiverAgent", null);
            AgentController advertisingColumn1 = container.createNewAgent("Slup",
                    "advertisingColumn.AdvertisingColumnAgent", null);

            advertisingColumn1.start();
            giver1.start();
            receiver1.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
