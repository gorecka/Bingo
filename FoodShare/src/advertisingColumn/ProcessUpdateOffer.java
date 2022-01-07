package advertisingColumn;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessUpdateOffer extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessUpdateOffer(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        AID giver = message.getSender();
        System.out.println(advertisingColumn.getAID().getName() + " ProcessUpdateOffer from " + giver.getName());
        JSONObject content = new JSONObject(message.getContent());

        ACLMessage reply = message.createReply();
        JSONObject replyContent = new JSONObject();
        replyContent.put("offerID", content.get("offerID"));

        // sprawdzenie czy nadawca jest autorem oferty
        // TODO
        Boolean isAuthor = true;

        if (isAuthor) {
            System.out.println(advertisingColumn.getAID().getName() + " Requester is the author, request accepted, updating offer");

            // aktualizacja oferty
            if (message.getOntology().equals(OntologyNames.EDITING_OFFER_ONTOLOGY)) {
                // edit the offer
                // TODO
            } else if (message.getOntology().equals(OntologyNames.DELETING_OFFER_ONTOLOGY)) {
                // delete the offer
                // TODO
            }

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.AGREE);
            advertisingColumn.send(reply);

            // pobranie listy zapisanych odbierających
            // TODO
            // poinformowanie zapisanych odbierających o aktualizacji oferty
            // TODO
        } else {
            System.out.println(advertisingColumn.getAID().getName() + " Requester is not the author, request refused");

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.REFUSE);
            advertisingColumn.send(reply);
        }
    }
}
