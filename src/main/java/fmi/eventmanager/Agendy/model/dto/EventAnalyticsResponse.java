package fmi.eventmanager.Agendy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventAnalyticsResponse {
    private List<FeedbackResponse> ratings;
    private List<TicketResponse> ticketBuyers;

    public EventAnalyticsResponse() { }
}
