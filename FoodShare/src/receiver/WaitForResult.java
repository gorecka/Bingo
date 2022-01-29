package receiver;

import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class WaitForResult extends CyclicBehaviour {
    ReceiverAgent receiver;

    WaitForResult(ReceiverAgent agent) {
        receiver = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + receiver.getAID().getName() + " chce odebrac wiadomosc");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformativeAgree = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtPerformativeRefuse = MessageTemplate.MatchPerformative(ACLMessage.REFUSE);
        MessageTemplate mtPerformative = MessageTemplate.or(mtPerformativeAgree, mtPerformativeRefuse);
        ACLMessage message = myAgent.receive(mtPerformative);

        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal wiadomość ");

            // przetworzenie otrzymanej wiadomości
            String ontology = message.getOntology();
            JSONObject content = new JSONObject(message.getContent());
            int performative = message.getPerformative();

            // decyzja co powinien zrobić odbiorca
            if (performative == ACLMessage.INFORM) {
                switch (ontology) {
                    case OntologyNames.EDITING_OFFER_ONTOLOGY:
                        System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o zmianie w ofercie " + content.get(JsonKeys.OFFER_ID));
                        System.out.println("content: " + content);
                        break;
                    case OntologyNames.DELETING_OFFER_ONTOLOGY:
                        System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o usunięciu oferty " + content.get(JsonKeys.OFFER_ID));
                        System.out.println("content: " + content);
                        break;
                    case OntologyNames.COLLECTION_DETAILS_ONTOLOGY:
                        System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o rezygnacji wystawiającego z negocjacji oferty \n" + content);
                        break;
                    case OntologyNames.CONFIRMATION_OF_RECEIPT:
                        System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany o zamknięciu zgłoszenia \n" + content);
                        break;
                }
            } else if (performative == ACLMessage.REFUSE) {
                switch (ontology) {
                    case OntologyNames.CONFIRMATION_OF_RECEIPT:
                        System.out.println("Agent " + receiver.getAID().getName() + " został poinformowany - nie udało się zamknąć oferty \n" + content);
                        break;
                }
            }
        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
