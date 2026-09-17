package fmi.eventmanager.Agendy.repository;

import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByTitle(String title);

    Event findByStatus(EventStatus status);

    List<Event> findByOrganizerId(Long organizerId);
}
