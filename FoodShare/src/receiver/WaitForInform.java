package receiver;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForInform extends CyclicBehaviour {
    ReceiverAgent receiver;

    WaitForInform(ReceiverAgent agent) {
        receiver = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + receiver.getAID().getName() + " chce odebrac wiadomosc");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage message = myAgent.receive(mtPerformative);

        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal wiadomość ");

            // przetworzenie otrzymanej wiadomości
            String ontology = message.getOntology();
            JSONObject content = new JSONObject(message.getContent());

            // decyzja co powinien zrobić odbiorca
            switch (ontology) {
                case OntologyNames.EDITING_OFFER_ONTOLOGY:
                    System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o zmianie w ofercie " + content.get("offerId"));
                    System.out.println("content: " + content);
                    break;
                case OntologyNames.DELETING_OFFER_ONTOLOGY:
                    System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o usunięciu oferty " + content.get("offerId"));
                    System.out.println("content: " + content);
                    break;
                case OntologyNames.COLLECTION_DETAILS_ONTOLOGY:
                    System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o rezygnacji wystawiającego z negocjacji oferty \n" + content);
                    break;
            }

        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
