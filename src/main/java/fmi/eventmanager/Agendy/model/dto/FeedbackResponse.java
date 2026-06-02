package fmi.eventmanager.Agendy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedbackResponse {
    private Long id;
    private Long eventId;
    private Long userId;
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public FeedbackResponse() { }
}
