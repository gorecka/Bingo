package advertisingColumn;

import advertisingColumn.data.ItemStatus;
import advertisingColumn.data.Offer;
import advertisingColumn.data.OfferStatus;
import advertisingColumn.data.User;
import jade.core.Agent;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdvertisingColumnAgent extends Agent {

    private List<User> users = new ArrayList<>();
    private List<Offer> offers = new ArrayList<>();

    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");
        addBehaviour(new WaitForRequest(this));
        //addBehaviour(new SendReviewForm(this, new AID("R1", AID.ISLOCALNAME), new AID("W1", AID.ISLOCALNAME).getName(), "Example offer"));
        addUsers();
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }

    protected List<Offer> getActiveOffers() {
        List<Offer> activeOffers = new ArrayList<>();
        // TODO: Docelowo pętlę for odkomentować, a usunąć linię 36 i 37
//        for (Offer o : offers) {
//            if(o.getOfferStatus() != OfferStatus.WAITING_FOR_GIVER) {
//                activeOffers.add(o);
//            }
//        }
        Offer offer1 = new Offer(1, "name", "description", OfferStatus.NEW, new Date(), new User("user1"), ItemStatus.FRESH);
        activeOffers.add(offer1);
        return activeOffers;
    }

    public Offer getOfferById(int offerId) {
        // dodawanie jednej oferty na potrzeby testów
        User user = new User("user1");
        Offer offer1 = new Offer(2345, "name", "description", OfferStatus.NEW, new Date(), user, ItemStatus.FRESH);
        List<User> possibleReceivers = new ArrayList<>();
        possibleReceivers.add(new User("user2"));
        possibleReceivers.add(new User("suspendedUser3"));
        offer1.setPossibleReceivers(possibleReceivers);

        offers.add(offer1);
        for (Offer offer : offers) {
            if (offer.getOfferId() == offerId) {
                return offer;
            }
        }
        return null;
    }

    public List<Offer> getOffersByAuthor(String username) {
        // dodawanie jednej oferty na potrzeby testów
        User user = new User("user1");
        Offer offer1 = new Offer(2345, "name", "description", OfferStatus.NEW, new Date(), user, ItemStatus.FRESH);
        List<User> possibleReceivers = new ArrayList<>();
        possibleReceivers.add(new User("user2"));
        possibleReceivers.add(new User("suspendedUser3"));
        offer1.setPossibleReceivers(possibleReceivers);
        offers.add(offer1);

        List<Offer> userOffers = new ArrayList<>();

        for (Offer offer : offers) {
            if (offer.getAuthor().getUsername().equals(username)) {
                userOffers.add(offer);
            }
        }
        return userOffers;
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

    public User getUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void addUsers() {
        User user1 = new User("user1");
        users.add(user1);

        User user2 = new User("user2");
        users.add(user2);

        User suspendedUser = new User("suspendedUser3");
        suspendedUser.setSuspended(true);
        users.add(suspendedUser);
    }

    public int publishOffer(String offerString, User giverUser) throws ParseException {
        JSONObject offerJson = new JSONObject(offerString);
        Offer offer = new Offer(offerJson, giverUser);
        offers.add(offer);

        int offerId = offer.getOfferId();
        return offerId;
    }

    public User addReviewForUser(User user, int newRating) {
        User updatedUser = this.addReview(user, newRating);
        users.forEach(el -> {
            if (el.getUsername().equals(updatedUser.getUsername()))
                users.set(users.indexOf(el), updatedUser);
        });
        return updatedUser;
    }

    private User addReview(User user, int newRating) {
        user.addRating(newRating);
        //sprawdzenie czy zablokowac uzytkownika
        if ((double) user.getRatingSum()/user.getRatingCount() < 2.0 && user.getRatingCount() > 2 && user.getNegativeRatingCount() > 2) {
            Date dt = new Date();
            user.setSuspended(true);
            user.setSuspensionStart(dt);
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            //uzytkownik blokowany na 14 dni
            c.add(Calendar.DATE, 14);
            dt = c.getTime();
            user.setSuspensionEnd(dt);
        }
        return user;
    }
}