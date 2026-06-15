package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.EventAnalyticsResponse;
import fmi.eventmanager.Agendy.model.dto.FeedbackRequest;
import fmi.eventmanager.Agendy.model.dto.FeedbackResponse;
import fmi.eventmanager.Agendy.model.entity.Feedback;
import fmi.eventmanager.Agendy.model.entity.Ticket;
import fmi.eventmanager.Agendy.model.entity.TicketStatus;
import fmi.eventmanager.Agendy.service.FeedbackService;
import fmi.eventmanager.Agendy.service.TicketService;
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
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final FeedbackService feedbackService;
    private final TicketService ticketService;

    public FeedbackController(FeedbackService feedbackService, TicketService ticketService) {
        this.feedbackService = feedbackService;
        this.ticketService = ticketService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> createFeedback(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestBody FeedbackRequest dto) {
        try {
            Feedback saved = feedbackService.saveFeedback(eventId, userId, dto.getRating(), dto.getComment());
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(saved, eventId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackResponse>> getEventFeedback(@PathVariable Long eventId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEvent(eventId);

        List<FeedbackResponse> response = feedbacks.stream()
                .map(f -> convertToResponse(f, eventId, f.getUser() != null ? f.getUser().getId() : null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics")
    public ResponseEntity<EventAnalyticsResponse> getEventAnalytics(@PathVariable Long eventId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEvent(eventId);
        List<Ticket> tickets = ticketService.getTicketsByEvent(eventId);

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