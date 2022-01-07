package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendCollectionDetails extends OneShotBehaviour {
    GiverAgent giver;
    public SendCollectionDetails(GiverAgent agent) {
        giver = agent;
    }

    @Override
    public void action() {
        System.out.println(giver.getAID().getName() + " zaraz wysle do słupa informację o ustalonych szczegółach odbioru");
        String content = "miejsce X, godzina Y";
        // przygotowanie wiadomości i wysłanie jej do słupa ogłoszeniowego
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Slup", AID.ISLOCALNAME));
        msg.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        msg.setContent(content);
        giver.send(msg);

        // oczekiwanie na odpowiedź
        giver.addBehaviour(new WaitForOfferUpdateConfirmation(giver));
    }
}
