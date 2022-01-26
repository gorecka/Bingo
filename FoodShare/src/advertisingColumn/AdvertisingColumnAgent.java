package advertisingColumn;

import advertisingColumn.data.ItemStatus;
import advertisingColumn.data.Offer;
import advertisingColumn.data.OfferStatus;
import advertisingColumn.data.User;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvertisingColumnAgent extends Agent {

    private List<User> users = new ArrayList<>();
    private List<Offer> offers = new ArrayList<>();

    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForRequest(this));
        //addBehaviour(new SendReviewForm(this, new AID("R1", AID.ISLOCALNAME), new AID("W1", AID.ISLOCALNAME).getName(), "Example offer"));
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }

    protected List<Offer> getActiveOffers() {
        List<Offer> activeOffers = new ArrayList<>();;
        // TODO: Docelowo pętlę for odkomentować, a usunąć linię 36 i 37
//        for (Offer o : offers) {
//            if(o.getOfferStatus() != OfferStatus.WAITING_FOR_GIVER) {
//                activeOffers.add(o);
//            }
//        }
        Offer offer1 = new Offer(1, "name", "description", OfferStatus.NEW, new Date(), new User(), ItemStatus.FRESH);
        activeOffers.add(offer1);
        return activeOffers;
    }

    public Offer getOfferById(int offerId) {
        Offer myOffer = new Offer();
        //Offer offer1 = new Offer(1, "name", "description", OfferStatus.NEW, new Date(), new User(), ItemStatus.FRESH);
        //offers.add(offer1);
        for (Offer offer : offers) {
            if (offer.getOfferId() == offerId) {
                myOffer = offer;
                break;
            }
        }
        return myOffer;
    }

    public void updateOffer(Offer newOffer) {
        offers.forEach(el -> {
            if (el.getOfferId() == newOffer.getOfferId())
                offers.set(offers.indexOf(el), newOffer);
        });
    }

    public void deleteOffer(int offerId) {
        offers.remove(offerId);
    }
}