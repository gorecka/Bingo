package giver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForReview extends CyclicBehaviour {
    GiverAgent giver;

    WaitForReview(GiverAgent agent) {
        giver = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " czekam na ocene");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology(OntologyNames.REVIEWING_FORM_ONTOLOGY);
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);
        ACLMessage message = giver.receive(mt);

        if (message != null) {
            String content = message.getContent();
            JSONObject obj = new JSONObject(content);
            String review = obj.getString("review");
            String reviewer = obj.getString("reviewer");
            String isBlocked = obj.getString("isBlocked");
            String avg = obj.getString("average");

            System.out.println("Agent " + giver.getAID().getName() + " otrzymal ocene " + review + " od uzytkownika " + reviewer);
            System.out.println("Agent " + giver.getAID().getName() + " ma srednia ocen " + avg + " i stan jego blokady to " + isBlocked);

        } else {
            System.out.println("Agent " + giver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
