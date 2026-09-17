package fmi.eventmanager.Agendy.repository;

import fmi.eventmanager.Agendy.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEventId(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}