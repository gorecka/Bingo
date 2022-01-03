package giver;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForPublishingOffer extends Behaviour {
    boolean isDone = false;
    GiverAgent giver;

    WaitForPublishingOffer(GiverAgent agent) {
        giver = agent;
    }

    @Override
    public void action() {
        // może tutaj powinno się wykorzystać conversation id?
        MessageTemplate mtOntology = MessageTemplate.MatchOntology("Publishing-offer-ontology");
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal wiadomość ");
            System.out.println("Treść wiadomości: " + message.getContent());
            int performative = message.getPerformative();
            if (performative == ACLMessage.AGREE) {
                System.out.println("Oferta została opublikowana");
            } else if (performative == ACLMessage.REFUSE) {
                System.out.println("Oferta nie została opublikowana");
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
