package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.model.entity.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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
}
