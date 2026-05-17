package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.TicketType;

import java.math.BigDecimal;

public class TicketRequest {
    private TicketType ticketType;
    private BigDecimal price;

    public TicketRequest() { }

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
}
