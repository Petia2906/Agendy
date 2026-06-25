package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.TicketRequest;
import fmi.eventmanager.Agendy.model.dto.TicketResponse;
import fmi.eventmanager.Agendy.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/events/{eventId}/tickets")
    public ResponseEntity<?> purchaseTicket(
            @PathVariable Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid TicketRequest dto) {
        try {
            TicketResponse response = ticketService.purchaseTicket(userId, eventId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/tickets/my")
    public ResponseEntity<List<TicketResponse>> getMyTickets(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ticketService.getMyTickets(userId));
    }

    @PatchMapping("/tickets/{ticketId}/cancel")
    public ResponseEntity<?> cancelTicket(@AuthenticationPrincipal Long userId,
                                          @PathVariable Long ticketId) {
        try {
            TicketResponse response = ticketService.cancelTicket(userId, ticketId);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}