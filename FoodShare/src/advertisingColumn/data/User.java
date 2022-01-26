package advertisingColumn.data;

import java.util.Date;

public class User {
    private String username;
    private int ratingSum;
    private int ratingCount;
    private int negativeRatingCount;
    private boolean isSuspended;
    private Date suspensionStart;
    private Date suspensionEnd;

    public void addRating(int newRating) {
        ratingSum += newRating;
        ratingCount++;
        if (newRating <= 2)
            negativeRatingCount++;
    }

    public User() {}

    public User(String name) {
        username = name;
        ratingSum = 0;
        ratingCount = 0;
        negativeRatingCount = 0;
        isSuspended = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(int ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getNegativeRatingCount() {
        return negativeRatingCount;
    }

    public void setNegativeRatingCount(int negativeRatingCount) {
        this.negativeRatingCount = negativeRatingCount;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public Date getSuspensionStart() {
        return suspensionStart;
    }

    public void setSuspensionStart(Date suspensionStart) {
        this.suspensionStart = suspensionStart;
    }

    public Date getSuspensionEnd() {
        return suspensionEnd;
    }

    public void setSuspensionEnd(Date suspensionEnd) {
        this.suspensionEnd = suspensionEnd;
    }
}