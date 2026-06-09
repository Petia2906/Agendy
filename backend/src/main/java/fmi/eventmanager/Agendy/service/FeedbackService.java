package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.Feedback;
import fmi.eventmanager.Agendy.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacksByEvent(Long eventId) {
        return feedbackRepository.findByEventId(eventId);
    }
}
