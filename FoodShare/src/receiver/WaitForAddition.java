package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForAddition extends Behaviour {
    boolean isDone;
    ReceiverAgent receiver;

    WaitForAddition(ReceiverAgent agent) {
        receiver = agent;
        isDone = false;
    }

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY);
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = receiver.receive(mt);
        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal potwierdzenie dopisania do oferty ");
            System.out.println("Treść wiadomości: " + message.getContent());
            // przetworzenie
            int performative = message.getPerformative();
            if (performative == ACLMessage.AGREE) {
                System.out.println("Dodano użytkownika na listę chętnych");
                receiver.addBehaviour(new WaitForProposal(receiver));
            } else if (performative == ACLMessage.REFUSE) {
                System.out.println("Nie dodano użytkownika na listę chętnych");
            }
            isDone = true;
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie otrzymal potwierdzenia dopisania do oferty - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
