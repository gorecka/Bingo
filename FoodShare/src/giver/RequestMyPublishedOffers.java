package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class RequestMyPublishedOffers extends OneShotBehaviour {
    GiverAgent giver;

    RequestMyPublishedOffers(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {

        System.out.println(giver.getAID().getName() + " zaraz wysle prosbe o liste opublikowanych ofert");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.GETTING_MY_PUBLISHED_OFFERS_ONTOLOGY);
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForListOfMyPublishedOffers(giver));
    }
}
