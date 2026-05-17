package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.TicketRequest;
import fmi.eventmanager.Agendy.model.dto.TicketResponse;
import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<?> purchaseTicket(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestBody TicketRequest dto) {
        try {

            Ticket ticketEntity = convertToEntity(dto);

            Ticket purchased = ticketService.purchaseTicket(eventId, userId, ticketEntity);

            // Връщаме безопасно ResponseDTO
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(purchased));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/tickets/my")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@RequestParam Long userId) {
        List<Ticket> tickets = ticketService.getMyTickets(userId);

        List<TicketResponse> responseDTOs = tickets.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @PatchMapping("/tickets/{ticketId}/cancel")
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) {
        try {
            Ticket cancelledTicket = ticketService.cancelTicket(ticketId);
            return ResponseEntity.ok(convertToResponseDTO(cancelledTicket));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private Ticket convertToEntity(TicketRequest dto) {
        Ticket ticket = new Ticket();
        ticket.setTicketType(dto.getTicketType());
        ticket.setPrice(dto.getPrice());
        return ticket;
    }

    private TicketResponse convertToResponseDTO(Ticket ticket) {
        TicketResponse dto = new TicketResponse();
        dto.setId(ticket.getId());
        dto.setTicketType(ticket.getTicketType());
        dto.setPrice(ticket.getPrice());
        dto.setStatus(ticket.getStatus());
        dto.setPurchasedAt(ticket.getPurchasedAt());

        if (ticket.getEvent() != null) {
            //dto.setEventId(ticket.getEvent().getId());
            //dto.setEventName(ticket.getEvent().getName());
        }
        if (ticket.getUser() != null) {
            dto.setUserId(ticket.getUser().getId());
        }

        return dto;
    }
}
