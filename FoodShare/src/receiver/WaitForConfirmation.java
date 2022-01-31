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
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = receiver.receive(mt);
        // decyzja co powinien zrobić odbiorca
        if  (message != null && message.getPerformative() == ACLMessage.AGREE) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal potwierdzenie rezygnacji z oferty ");
            System.out.println("Treść wiadomości: " + message.getContent());
            isDone = true;
        } else if (message != null && message.getPerformative() == ACLMessage.REFUSE) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal odmowe rezygnacji z oferty ");
            System.out.println("Treść wiadomości: " + message.getContent());
            isDone = true;
        }
        else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal potwierdzenia rezygnacji z oferty - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
