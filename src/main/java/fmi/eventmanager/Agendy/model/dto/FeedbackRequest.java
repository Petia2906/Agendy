package fmi.eventmanager.Agendy.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private int rating;
    private String comment;

    public FeedbackRequest() { }
}
