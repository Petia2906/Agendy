package fmi.eventmanager.Agendy.repository;

import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findByEvent(Event event);
}
