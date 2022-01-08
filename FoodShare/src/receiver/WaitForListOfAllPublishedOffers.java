package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForListOfAllPublishedOffers extends Behaviour {
    boolean isDone = false;
    ReceiverAgent receiver;

    WaitForListOfAllPublishedOffers(ReceiverAgent agent) {
        receiver = agent;
    }

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.GETTING_ALL_PUBLISHED_OFFERS_ONTOLOGY);
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal wiadomość ");
            int performative = message.getPerformative();
            if (performative == ACLMessage.INFORM) {
                System.out.println("Otrzymano listę opublikowanych ofert (lista może być pusta)");
                System.out.println("Treść otrzymanej wiadomości: " + message.getContent());
            } else if (performative == ACLMessage.REFUSE) {
                System.out.println("Wystąpił błąd w wyniku którego nie udało się pobrać listy opublikowanych ofert");
            }
            isDone = true;
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
