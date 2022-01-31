package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForReceiverResignationConfirmation extends Behaviour {

    GiverAgent giver;
    boolean isDone = false;

    public WaitForReceiverResignationConfirmation(GiverAgent giver) {
        this.giver = giver;
    }

    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " czekam na potwierdzenie zapisu rezygnacji z odbierającego od słupa");

        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = giver.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal informacje od słupa o usunięciu odbiorcy z listy");
            isDone = true;
        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal potwierdzenia od słupa o usunięciu odbiorcy z listy");
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
