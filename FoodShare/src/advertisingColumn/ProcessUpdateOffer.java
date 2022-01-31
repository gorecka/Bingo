package advertisingColumn;

import advertisingColumn.data.ItemStatus;
import advertisingColumn.data.Offer;
import advertisingColumn.data.User;
import communicationConstants.JsonKeys;
import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

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
        int offerId = content.getInt(JsonKeys.OFFER_ID);

        ACLMessage reply = message.createReply();
        JSONObject replyContent = new JSONObject();
        replyContent.put(JsonKeys.OFFER_ID, offerId);

        Offer offer = advertisingColumn.getOfferById(offerId);

        boolean isAuthor = offer.getAuthor().getUsername().equals(message.getSender().getName());

        if (isAuthor) {
            System.out.println(advertisingColumn.getAID().getName() + " Requester is the author, request accepted, updating offer");

            ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
            // aktualizacja oferty
            if (message.getOntology().equals(OntologyNames.EDITING_OFFER_ONTOLOGY)) {
                // edit the offer
                try {
                    offer.setBestBeforeDate(content.getString(JsonKeys.OFFER_BEST_BEFORE_DATE));
                    offer.setDescription(content.getString(JsonKeys.OFFER_DESCRIPTION));
                    offer.setItemStatus(content.getEnum(ItemStatus.class, JsonKeys.OFFER_ITEM_STATUS));
                    offer.setName(content.getString(JsonKeys.OFFER_NAME));
                    advertisingColumn.updateOffer(offer);
                } catch (ParseException e) {
                    System.out.println(advertisingColumn.getAID().getName() + " failed to parse bestBeforeDate");
                }
                informMsg.setOntology(OntologyNames.EDITING_OFFER_ONTOLOGY);
            } else if (message.getOntology().equals(OntologyNames.DELETING_OFFER_ONTOLOGY)) {
                // delete the offer
                advertisingColumn.deleteOffer(offerId);
                informMsg.setOntology(OntologyNames.DELETING_OFFER_ONTOLOGY);
            }

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.AGREE);
            advertisingColumn.send(reply);

            // pobranie listy zapisanych odbierających
            List<User> receivers = offer.getPossibleReceivers();

            // poinformowanie zapisanych odbierających o aktualizacji oferty
            JSONObject informMsgContent = new JSONObject(offer);
            informMsg.setContent(informMsgContent.toString());
            receivers.forEach(el -> {
                informMsg.addReceiver(new AID(el.getUsername(), AID.ISLOCALNAME));
                advertisingColumn.send(informMsg);
            });
        } else {
            System.out.println(advertisingColumn.getAID().getName() + " Requester is not the author, request refused");

            // wysłanie odpowiedzi
            reply.setPerformative(ACLMessage.REFUSE);
            advertisingColumn.send(reply);
        }
    }
}
