package advertisingColumn;

import advertisingColumn.data.ItemStatus;
import advertisingColumn.data.Offer;
import advertisingColumn.data.OfferStatus;
import advertisingColumn.data.User;
import jade.core.AID;
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
        addBehaviour(new SendReviewForm(this, 1357)); // 60
        addUsers();
//        addOffers();
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }

    protected List<Offer> getActiveOffers() {
        List<Offer> activeOffers = new ArrayList<>();
        for (Offer o : offers) {
            if(o.getOfferStatus() == OfferStatus.OPEN) {
                activeOffers.add(o);
            }
        }
        return activeOffers;
    }

    public Offer getOfferById(int offerId) {
        for (Offer offer : offers) {
            if (offer.getOfferId() == offerId) {
                return offer;
            }
        }
        return null;
    }

    public List<Offer> getOffersByAuthor(String username) {
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

        User receiver1 = new User("receiver1");
        users.add(receiver1);

        User suspendedUser = new User("suspendedUser3");
        suspendedUser.setSuspended(true);
        users.add(suspendedUser);
    }

    private void addOffers() {
        User user = new User("user1");
        Offer offer1 = new Offer(2345, "ser", "ser w plastrach", OfferStatus.OPEN, new Date(), user, ItemStatus.FRESH);
        List<User> possibleReceivers = new ArrayList<>();
        possibleReceivers.add(new User("user2"));
        possibleReceivers.add(new User("suspendedUser3"));
        offer1.setPossibleReceivers(possibleReceivers);
        offer1.setChosenReceiver(new User("user2"));
        offers.add(offer1);
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
        if ((double) user.getRatingSum()/user.getRatingCount() < 2.5 && user.getRatingCount() > 2 && user.getNegativeRatingCount() > 2) {
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
