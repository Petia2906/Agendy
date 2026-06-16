package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.Speaker;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.SpeakerRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final UserRepository userRepository;

    public SpeakerService(SpeakerRepository speakerRepository, UserRepository userRepository) {
        this.speakerRepository = speakerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Speaker createSpeaker(Long userId, Speaker speaker) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user making request"));
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can add speakers!");
        }
        if (speakerRepository.existsByEmail(speaker.getEmail())) {
            throw new IllegalArgumentException("Email '" + speaker.getEmail() + "' is already taken!");
        }
        return speakerRepository.save(speaker);
    }
}
