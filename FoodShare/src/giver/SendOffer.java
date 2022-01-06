package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendOffer extends OneShotBehaviour {
    GiverAgent giver;

    SendOffer(GiverAgent agent){
        giver = agent;
    }

    @Override
    public void action() {
        // przygotowanie oferty
        //TODO
        String content = "Produkt: ser, stan: nieotwarty, data ważności: 14.01.2022";

        System.out.println(giver.getAID().getName() + " zaraz wysle wiadomosc do slupa");
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.PUBLISHING_OFFER_ONTOLOGY);
        msg.setContent(content);
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForPublishingOffer(giver));
    }
}
