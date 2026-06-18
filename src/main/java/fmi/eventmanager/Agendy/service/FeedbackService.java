package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.EventStatus;
import fmi.eventmanager.Agendy.model.entity.Feedback;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.FeedbackRepository;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           EventRepository eventRepository,
                           UserRepository userRepository,
                           TicketRepository ticketRepository) {
        this.feedbackRepository = feedbackRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public Feedback saveFeedback(Long eventId, Long userId, int rating, String comment) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (event.getStatus() != EventStatus.PAST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event has not passed yet!");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean hasTicket = ticketRepository.existsByEventIdAndUserId(eventId, userId);
        if (!hasTicket) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must have a ticket to leave feedback");
        }
        boolean alreadyReviewed = feedbackRepository.existsByEventIdAndUserId(eventId, userId);
        if (alreadyReviewed) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already left feedback for this event");
        }
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Feedback feedback = new Feedback();
        feedback.setEvent(event);
        feedback.setUser(user);
        feedback.setRating(rating);
        feedback.setComment(comment);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacksByEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return feedbackRepository.findByEventId(eventId);
    }

    //use this method for admin analytics maybe?
    public Map<String, Object> getAnalytics(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);
        if (feedbacks.isEmpty()) {
            return Map.of(
                    "eventId", eventId,
                    "totalFeedbacks", 0,
                    "averageRating", 0.0
            );
        }
        double averageRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        Map<Integer, Long> ratingDistribution = new java.util.HashMap<>();
        for (int i = 1; i <= 5; i++) {
            final int r = i;
            ratingDistribution.put(r, feedbacks.stream()
                    .filter(f -> f.getRating() == r)
                    .count());
        }
        return Map.of(
                "eventId", eventId,
                "totalFeedbacks", feedbacks.size(),
                "averageRating", Math.round(averageRating * 10.0) / 10.0,
                "ratingDistribution", ratingDistribution
        );
    }
}