package advertisingColumn;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForFilledInReview extends CyclicBehaviour {
    AdvertisingColumnAgent advertisingColumn;

    WaitForFilledInReview(AdvertisingColumnAgent agent) {
        advertisingColumn = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + advertisingColumn.getAID().getName() + " chce odebrac wiadomosc");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology("Reviewing-form-ontology");
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);
        ACLMessage message = advertisingColumn.receive(mt);

        if (message != null) {
            System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal wypelnniona ankiete ");
            advertisingColumn.addBehaviour(new ProcessReviewForm(advertisingColumn, message));

        } else {
            System.out.println("Agent " + advertisingColumn.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
