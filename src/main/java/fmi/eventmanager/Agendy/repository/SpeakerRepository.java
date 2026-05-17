package fmi.eventmanager.Agendy.repository;

import fmi.eventmanager.Agendy.model.entity.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    boolean existsByEmail(String email);
}
