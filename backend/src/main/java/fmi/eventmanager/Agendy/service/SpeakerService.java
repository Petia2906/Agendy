package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.Speaker;
import fmi.eventmanager.Agendy.repository.SpeakerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SpeakerService {
    private final SpeakerRepository speakerRepository;

    public SpeakerService(SpeakerRepository speakerRepository) {
        this.speakerRepository = speakerRepository;
    }

    @Transactional
    public Speaker createSpeaker(Speaker speaker) {
        if (speakerRepository.existsByEmail(speaker.getEmail())) {
            throw new IllegalArgumentException("Email '" + speaker.getEmail() + "' is already taken!");
        }
        return speakerRepository.save(speaker);
    }
}
