package fmi.eventmanager.Agendy.repository;

import fmi.eventmanager.Agendy.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByEventId(Long eventId);
}
