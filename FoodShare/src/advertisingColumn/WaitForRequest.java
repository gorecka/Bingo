package advertisingColumn;

import communicationConstants.OntologyNames;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForRequest extends CyclicBehaviour {
    AdvertisingColumnAgent advertisingColumn;

    WaitForRequest(AdvertisingColumnAgent agent) {
        advertisingColumn = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + advertisingColumn.getAID().getName() + " chce odebrac wiadomosc");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage message = myAgent.receive(mtPerformative);

        if (message != null) {
            //System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal wiadomość ");

            // przetworzenie otrzymanej wiadomości
            String ontology = message.getOntology();

            // decyzja co powinien zrobić słup ogłoszeniowy
            if (ontology.equals(OntologyNames.PUBLISHING_OFFER_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żądanie publikacji oferty ");
                advertisingColumn.addBehaviour(new PublishOffer(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.GETTING_MY_PUBLISHED_OFFERS_ONTOLOGY)){
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żądanie listy ofert agenta");
                advertisingColumn.addBehaviour(new GetMyPublishedOffers(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.GETTING_POSSIBLE_RECEIVERS_ONTOLOGY)){
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żadanie listy chętnych na ofertę ");
                advertisingColumn.addBehaviour(new GetPossibleReceivers(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.RESIGNATION_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żądanie rezygnacji z oferty");
                advertisingColumn.addBehaviour(new ProcessResignation(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.GETTING_ALL_PUBLISHED_OFFERS_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żadanie listy wszystkich ofert");
                advertisingColumn.addBehaviour(new GetAllPublishedOffers(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.EDITING_OFFER_ONTOLOGY) ||
                       ontology.equals(OntologyNames.DELETING_OFFER_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żądanie modyfikacji oferty ");
                advertisingColumn.addBehaviour(new ProcessUpdateOffer(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.CONFIRMATION_OF_RECEIPT)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal potwierdzenie odbioru ");
                advertisingColumn.addBehaviour(new CloseOffer(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.SIGNING_UP_FOR_OFFER_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal żądanie zapisu do oferty ");
                advertisingColumn.addBehaviour(new ProcessEnrolment(advertisingColumn, message));
            } else if (ontology.equals(OntologyNames.COLLECTION_DETAILS_ONTOLOGY)) {
                System.out.println("Agent " + advertisingColumn.getAID().getName() + " otrzymal informacje o szczegółach odbior ");
                advertisingColumn.addBehaviour(new ProcessUpdateOfferStatus(advertisingColumn, message));
            }

        } else {
            System.out.println("Agent " + advertisingColumn.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}
