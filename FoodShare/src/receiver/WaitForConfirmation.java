package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForConfirmation extends Behaviour {
    boolean isDone;
    ReceiverAgent receiver;

    WaitForConfirmation(ReceiverAgent agent) {
        receiver = agent;
        isDone = false;
    }

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = receiver.receive(mt);
        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal potwierdzenie rezygnacji z oferty ");
            System.out.println("Treść wiadomości: " + message.getContent());
            isDone = true;
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal potwierdzenia rezygnacji z oferty - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
