package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.CreateSpeakerRequest;
import fmi.eventmanager.Agendy.model.dto.SpeakerResponse;
import fmi.eventmanager.Agendy.model.dto.UpdateSpeakerRequest;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.Speaker;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.SpeakerRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SpeakerService(SpeakerRepository speakerRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.speakerRepository = speakerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Speaker createSpeaker(Long userId, CreateSpeakerRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user making request"));
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can add speakers!");
        }
        if (speakerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already taken!");
        }

        Speaker speaker = new Speaker();
        speaker.setName(request.getName());
        speaker.setEmail(request.getEmail());
        speaker.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        speaker.setBio(request.getBio());
        speaker.setPhotoUrl(request.getPhotoUrl());
        speaker.setOrganization(request.getOrganization());

        return speakerRepository.save(speaker);
    }

    @Transactional
    public SpeakerResponse updateSpeaker(Long userId, Long speakerId, UpdateSpeakerRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user making request"));
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can edit speakers!");
        }
        Speaker speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Speaker not found"));

        speaker.setName(request.getName());
        speaker.setBio(request.getBio());
        speaker.setPhotoUrl(request.getPhotoUrl());
        speaker.setOrganization(request.getOrganization());

        return mapToResponse(speakerRepository.save(speaker));
    }

    public List<SpeakerResponse> getAllSpeakers() {
        return speakerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private SpeakerResponse mapToResponse(Speaker speaker) {
        SpeakerResponse response = new SpeakerResponse();
        response.setId(speaker.getId());
        response.setName(speaker.getName());
        response.setEmail(speaker.getEmail());
        response.setBio(speaker.getBio());
        response.setPhotoUrl(speaker.getPhotoUrl());
        response.setOrganization(speaker.getOrganization());
        return response;
    }
}
