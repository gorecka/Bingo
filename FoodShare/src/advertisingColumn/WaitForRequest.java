package advertisingColumn;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForRequest extends CyclicBehaviour {

    AdvertisingColumnAgent advertisingColumn;

    WaitForRequest(AdvertisingColumnAgent agent) {
        advertisingColumn = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + advertisingColumn.getAID().getName() + " chce odebrac wiadomosc");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage message = myAgent.receive(mtPerformative);

        if (message != null) {
            System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal wiadomość ");

            // przetworzenie otrzymanej wiadomości
            String ontology = message.getOntology();

            // decyzja co powinien zrobić słup ogłoszeniowy
            if (ontology == "Publishing-offer-ontology") {
                advertisingColumn.addBehaviour(new PublishOffer(advertisingColumn, message));
            }

        } else {
            System.out.println("Agent " + advertisingColumn.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
