package fmi.eventmanager.Agendy.model.dto;

public class FeedbackRequest {
    private int rating;
    private String comment;

    public FeedbackRequest() { }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
