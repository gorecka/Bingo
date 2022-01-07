package advertisingColumn;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessDeleteOffer extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessDeleteOffer(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        System.out.println("ProcessDeleteOffer");
        AID giver = message.getSender();
        JSONObject content = new JSONObject(message.getContent());

        ACLMessage reply = message.createReply();
//        reply.setOntology(OntologyNames.DELETING_OFFER_ONTOLOGY);
//        reply.addReceiver(giver);

        // sprawdzenie czy nadawca jest autorem oferty
        // TODO
        Boolean isAuthor = true;

        if (isAuthor) {
            System.out.println("Requester is the author, request accepted, deleting offer");

            // usunięcie oferty
            // TODO

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.AGREE);
            advertisingColumn.send(reply);

            // pobranie listy zapisanych odbierających
            // TODO
            // poinformowanie zapisanych odbierających o aktualizacji oferty
            // TODO
        } else {
            System.out.println("Requester is not the author, request refused");

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.REFUSE);
            advertisingColumn.send(reply);
        }
    }
}
