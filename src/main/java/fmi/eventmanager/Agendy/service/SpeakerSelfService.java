package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Events.EventResponse;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.Session;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.SessionRepository;
import fmi.eventmanager.Agendy.repository.SpeakerRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpeakerSelfService {
    private final SpeakerRepository speakerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;
    private final EventService eventService;

    public SpeakerSelfService(SpeakerRepository speakerRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, SessionRepository sessionRepository,
                          EventService eventService) {
        this.speakerRepository = speakerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionRepository = sessionRepository;
        this.eventService = eventService;
    }

    public List<EventResponse> getEventsForSpeaker(Long speakerUserId) {
        User user = userRepository.findById(speakerUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.SPEAKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only speakers can view this!");
        }

        List<Session> sessions = sessionRepository.findBySpeakerId(speakerUserId);

        Map<Long, Event> distinctEvents = new LinkedHashMap<>();
        for (Session session : sessions) {
            Event event = session.getEvent();
            distinctEvents.putIfAbsent(event.getId(), event);
        }

        return distinctEvents.values()
                .stream()
                .map(eventService::mapToResponse)
                .toList();
    }
}