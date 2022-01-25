package advertisingColumn.data;

import java.util.Date;
import java.util.List;

public class Offer {
    private int offerId;
    private String name;
    private String description;
    private String state; // enum? jakie mogą być stany?
    private Date bestBeforeDate;
    private Date creationDate;

    private User author;
    private List<User> possibleReceivers;
    private User chosenReceiver;

    private String status; // zrobić enum?
    private String receiptPlace;
    private Date receiptDate;
    private Date receiptDetailsSettled;
    private Date receiptConfirmationDate;
    private int rating;
    private Date reviewDate;
}