package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TicketRequest {
    private TicketType ticketType;
    private BigDecimal price;

    public TicketRequest() { }
}
