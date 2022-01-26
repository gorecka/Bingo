package advertisingColumn.data;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Offer {
    private int offerId;
    private String name;
    private String description;
    private OfferStatus offerStatus;
    private Date bestBeforeDate;
    private Date creationDate;

    private User author;
    private List<User> possibleReceivers;
    private User chosenReceiver;

    private ItemStatus itemStatus;
    private String receiptPlace;
    private Date receiptDate;
    private Date receiptDetailsSettled;
    private Date receiptConfirmationDate;
    private int rating;
    private Date reviewDate;

    public Offer() {}
    public Offer(int offerId, String name, String description, OfferStatus offerStatus, Date bestBeforeDate, User author, ItemStatus itemStatus) {
        this.offerId = offerId;
        this.name = name;
        this.description = description;
        this.offerStatus = offerStatus;
        this.bestBeforeDate = bestBeforeDate;
        creationDate = new Date();
        this.author = author;
        this.itemStatus = itemStatus;
    }

    public JSONObject toJSON() {
        JSONObject offer = new JSONObject();
        offer.put("offerID", offerId);
        offer.put("name", name);
        offer.put("status", itemStatus);
        offer.put("bestBeforeDate", bestBeforeDate);
        offer.put("description", description);
        offer.put("author", author);
        offer.put("creationDate", creationDate);
        return offer;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(Date bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }
    public void setBestBeforeDate(String bestBeforeDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        this.bestBeforeDate = formatter.parse(bestBeforeDate);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<User> getPossibleReceivers() {
        return possibleReceivers;
    }

    public void setPossibleReceivers(List<User> possibleReceivers) {
        this.possibleReceivers = possibleReceivers;
    }

    public User getChosenReceiver() {
        return chosenReceiver;
    }

    public void setChosenReceiver(User chosenReceiver) {
        this.chosenReceiver = chosenReceiver;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getReceiptPlace() {
        return receiptPlace;
    }

    public void setReceiptPlace(String receiptPlace) {
        this.receiptPlace = receiptPlace;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getReceiptDetailsSettled() {
        return receiptDetailsSettled;
    }

    public void setReceiptDetailsSettled(Date receiptDetailsSettled) {
        this.receiptDetailsSettled = receiptDetailsSettled;
    }

    public Date getReceiptConfirmationDate() {
        return receiptConfirmationDate;
    }

    public void setReceiptConfirmationDate(Date receiptConfirmationDate) {
        this.receiptConfirmationDate = receiptConfirmationDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}