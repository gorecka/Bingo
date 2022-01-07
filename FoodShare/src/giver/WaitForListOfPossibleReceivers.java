package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForListOfPossibleReceivers extends Behaviour {
    boolean isDone = false;
    GiverAgent giver;

    WaitForListOfPossibleReceivers(GiverAgent agent) {
        giver = agent;
    }
    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.GETTING_POSSIBLE_RECEIVERS_ONTOLOGY);
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal wiadomość o liście chętnych");
            System.out.println("Treść wiadomości: " + message.getContent());
            int performative = message.getPerformative();
            if (performative == ACLMessage.INFORM) {
                System.out.println("Otrzymano listę chętnych (lista może być pusta)");
                giver.addBehaviour(new SendProposal(giver));
            } else if (performative == ACLMessage.REFUSE) {
                System.out.println("Wystąpił błąd w wyniku którego nie udało się pobrać listy chętnych");
            }
            isDone = true;
        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
