package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    // private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket purchaseTicket(Long eventId, Long userId, Ticket targetTicket) {
        // Event event = eventRepository.findById(eventId).orElseThrow(...);
        User user = userRepository.findById(userId).orElseThrow(/*add ex later*/);

        targetTicket.setStatus(TicketStatus.PURCHASED);
        // targetTicket.setEvent(event);
        targetTicket.setUser(user);

        return ticketRepository.save(targetTicket);
    }

    public List<Ticket> getMyTickets(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Transactional
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled.");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        return ticketRepository.save(ticket);
    }
}