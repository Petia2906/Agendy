package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    private TicketType ticketType;

    public TicketRequest() { }
}
