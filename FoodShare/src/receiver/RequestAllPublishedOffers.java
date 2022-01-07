package receiver;


import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestAllPublishedOffers extends OneShotBehaviour {
    ReceiverAgent receiver;

    RequestAllPublishedOffers(ReceiverAgent agent){
        receiver = agent;
    }

    @Override
    public void action() {

        System.out.println(receiver.getAID().getName() + " zaraz wysle prosbe o liste wszystkich opublikowanych ofert");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.GETTING_ALL_PUBLISHED_OFFERS_ONTOLOGY);
        receiver.send(msg);

        // oczekiwanie na odpowiedź
        receiver.addBehaviour(new WaitForListOfAllPublishedOffers(receiver));
    }

}
