package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.model.entity.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketResponse {

    private Long id;
    private Long eventId;
    private String eventName;
    private Long userId;
    private TicketType ticketType;
    private BigDecimal price;
    private TicketStatus status;
    private LocalDateTime purchasedAt;

    public TicketResponse() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
