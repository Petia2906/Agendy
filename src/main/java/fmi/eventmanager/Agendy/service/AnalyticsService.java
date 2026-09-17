package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.EventAnalyticsResponse;
import fmi.eventmanager.Agendy.model.dto.FeedbackResponse;
import fmi.eventmanager.Agendy.model.dto.TicketResponse;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Feedback;
import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.FeedbackRepository;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AnalyticsService {
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public AnalyticsService(EventRepository eventRepository,
                            FeedbackRepository feedbackRepository,
                            TicketRepository ticketRepository,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public EventAnalyticsResponse getAnalytics(Long eventId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (!event.getOrganizerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the organizer of the event " +
                    "and cannot view in debt analytics.");
        }

        //not cute at all, redo it later
        EventAnalyticsResponse eventAnalyticsResponse = new EventAnalyticsResponse();
        eventAnalyticsResponse.setRatings(feedbackRepository.findByEventId(eventId)
                .stream().map(feedback -> convertToResponse(feedback))
                .toList());

        eventAnalyticsResponse.setTicketBuyers(ticketRepository.findByEventId(eventId)
                .stream().map(ticket -> mapToResponse(ticket))
                .toList());

        return eventAnalyticsResponse;
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        FeedbackResponse dto = new FeedbackResponse();
        dto.setId(feedback.getId());
        dto.setEventId(feedback.getEvent().getId());
        dto.setUserId(feedback.getUser().getId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        if (feedback.getUser() != null) {
            dto.setUserName(feedback.getUser().getName());
        }
        return dto;
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setEventId(ticket.getEvent().getId());
        response.setEventTitle(ticket.getEvent().getTitle());
        response.setUserId(ticket.getUser().getId());
        response.setUsername(ticket.getUser().getName());
        response.setEmail(ticket.getUser().getEmail());
        response.setTicketType(ticket.getTicketType());
        response.setPrice(ticket.getPrice());
        response.setStatus(ticket.getStatus());
        response.setPurchasedAt(ticket.getPurchasedAt());
        return response;
    }
}
