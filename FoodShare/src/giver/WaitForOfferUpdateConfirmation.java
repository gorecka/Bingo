package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForOfferUpdateConfirmation extends Behaviour {
    GiverAgent giver;
    boolean isDone = false;

    public WaitForOfferUpdateConfirmation(GiverAgent giver) {
        this.giver = giver;
    }

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = giver.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal informacje od słupa o zaktualizowaniu informacji o ofercie: zapisano odbiorcę, termin i miejsce ");
            System.out.println("Treść wiadomości: " + message.getContent());
            isDone = true;
        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal potwierdzenia od słupa o przyjęciu danych o statusie oferty przez słup - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
