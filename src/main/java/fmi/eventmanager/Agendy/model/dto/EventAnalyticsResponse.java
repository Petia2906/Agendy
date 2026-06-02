package fmi.eventmanager.Agendy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EventAnalyticsResponse {
    private Long eventId;
    private long totalTicketsSold;
    private BigDecimal totalRevenue;
    private double averageRating;
    private long totalFeedbacksCount;

    public EventAnalyticsResponse() { }
}
