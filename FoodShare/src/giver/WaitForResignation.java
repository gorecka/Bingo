package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForResignation extends CyclicBehaviour {
    GiverAgent giver;

    @Override
    public void action() {
        // czekanie na wiadomość pasującą do wzorca
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.RESIGNATION_ONTOLOGY);
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);

        ACLMessage message = giver.receive(mt);
        if (message != null) {
            System.out.println("Agent " + giver.getAID().getName() + " otrzymal informacje o rezygnacji z oferty ");
            System.out.println("Treść wiadomości: " + message.getContent());

            //uaktualnienie jego listy chetnych??
            //TODO


        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal rezygnacji z oferty - blokada");
            block();
        }
    }
}
