package fmi.eventmanager.Agendy.model.dto;

import java.math.BigDecimal;

public class EventAnalyticsResponse {
    private Long eventId;
    private long totalTicketsSold;
    private BigDecimal totalRevenue;
    private double averageRating;
    private long totalFeedbacksCount;

    public EventAnalyticsResponse() { }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public long getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(long totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getTotalFeedbacksCount() {
        return totalFeedbacksCount;
    }

    public void setTotalFeedbacksCount(long totalFeedbacksCount) {
        this.totalFeedbacksCount = totalFeedbacksCount;
    }
}
