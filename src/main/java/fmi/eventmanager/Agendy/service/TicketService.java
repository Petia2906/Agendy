package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.TicketRequest;
import fmi.eventmanager.Agendy.model.dto.TicketResponse;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
                         EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketResponse purchaseTicket(Long userId, Long eventId, TicketRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (user.getRole() != Role.ATTENDEE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only attendees can purchase tickets!");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found!"));

        boolean alreadyPurchased = ticketRepository.existsByEventIdAndUserId(eventId, userId);
        if (alreadyPurchased) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have a ticket for this event!");
        }

        long soldTickets = ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.PURCHASED);
        if (soldTickets >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is fully booked!");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setTicketType(request.getTicketType());
        ticket.setPrice(request.getPrice());
        ticket.setStatus(TicketStatus.PURCHASED);
        Ticket saved = ticketRepository.save(ticket);
        return mapToResponse(saved);
    }

    public List<TicketResponse> getMyTickets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        return ticketRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public TicketResponse cancelTicket(Long userId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found!"));

        if (!ticket.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This is not your ticket!");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket is already cancelled!");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        return mapToResponse(ticketRepository.save(ticket));
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setEventId(ticket.getEvent().getId());
        response.setEventTitle(ticket.getEvent().getTitle());
        response.setUserId(ticket.getUser().getId());
        response.setTicketType(ticket.getTicketType());
        response.setPrice(ticket.getPrice());
        response.setStatus(ticket.getStatus());
        response.setPurchasedAt(ticket.getPurchasedAt());
        return response;
    }
}