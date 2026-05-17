package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.EventAnalyticsResponse;
import fmi.eventmanager.Agendy.model.dto.FeedbackRequest;
import fmi.eventmanager.Agendy.model.dto.FeedbackResponse;
import fmi.eventmanager.Agendy.model.entity.Feedback;
import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.repository.FeedbackRepository;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events/{eventId}")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    public FeedbackController(FeedbackRepository feedbackRepository, TicketRepository ticketRepository) {
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> createFeedback(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestBody FeedbackRequest dto) {
        try {
            // fix magic numbers later, dto to entity conversion and so on
            if (dto.getRating() < 1 || dto.getRating() > 5) {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
            }

            Feedback feedback = new Feedback();
            feedback.setRating(dto.getRating());
            feedback.setComment(dto.getComment());

            Feedback saved = feedbackRepository.save(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(saved, eventId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackResponse>> getEventFeedback(@PathVariable Long eventId) {
        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);

        List<FeedbackResponse> response = feedbacks.stream()
                .map(f -> convertToResponse(f, eventId, f.getUser() != null ? f.getUser().getId() : null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics")
    public ResponseEntity<EventAnalyticsResponse> getEventAnalytics(@PathVariable Long eventId) {
        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);

        //fix later when you have event logic
        List<Ticket> tickets = ticketRepository.findAll().stream()
                .filter(t -> t.getEvent() != null && t.getEvent().getId().equals(eventId))
                .collect(Collectors.toList());

        long soldTicketsCount = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.PURCHASED)
                .count();

        BigDecimal totalRevenue = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.PURCHASED)
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double avgRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        EventAnalyticsResponse analytics = new EventAnalyticsResponse();
        analytics.setEventId(eventId);
        analytics.setTotalTicketsSold(soldTicketsCount);
        analytics.setTotalRevenue(totalRevenue);
        analytics.setAverageRating(Math.round(avgRating * 10.0) / 10.0);
        analytics.setTotalFeedbacksCount(feedbacks.size());
        return ResponseEntity.ok(analytics);
    }
    // fix later, make appropriate constructor
    // make it PRETYYY

    private FeedbackResponse convertToResponse(Feedback feedback, Long eventId, Long userId) {
        FeedbackResponse dto = new FeedbackResponse();
        dto.setId(feedback.getId());
        dto.setEventId(eventId);
        dto.setUserId(userId);
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        if (feedback.getUser() != null) {
            dto.setUserName(feedback.getUser().getName());
        }
        return dto;
    }
}
