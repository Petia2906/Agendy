package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Sessions.CreateSessionRequest;
import fmi.eventmanager.Agendy.model.dto.Sessions.SessionResponse;
import fmi.eventmanager.Agendy.model.dto.Sessions.UpdateSessionRequest;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Hall;
import fmi.eventmanager.Agendy.model.entity.Session;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.HallRepository;
import fmi.eventmanager.Agendy.repository.SessionRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository,
                          HallRepository hallRepository,
                          UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.userRepository = userRepository;
    }

    // create session for event
    public SessionResponse createSession(Long userId, Long eventId, CreateSessionRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (userId != event.getOrganizerId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only event organizers can add sessions!");
        }

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));
        validateSessionTimes(event, request.getStartTime(), request.getEndTime());

        Session session = new Session(
                event,
                null,
                hall,
                request.getTitle(),
                request.getDescription(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (request.getSpeakerId() != null) {
            User speaker = userRepository.findById(request.getSpeakerId())
                    .orElseThrow(() -> new RuntimeException("Speaker not found"));
            session.setSpeaker(speaker);
        }

        return mapToResponse(sessionRepository.save(session));
    }

    // get all sessions for event
    public List<SessionResponse> getSessionsByEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Session> sessions = sessionRepository.findByEventOrderByStartTimeAsc(event);
        List<SessionResponse> responses = new ArrayList<>();
        for (Session s : sessions) {
            responses.add(mapToResponse(s));
        }
        return responses;
    }

    // update session
    public SessionResponse updateSession(Long userId, Long sessionId, UpdateSessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (userId != session.getEvent().getOrganizerId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only event organizers can add sessions!");
        }

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        validateSessionTimes(session.getEvent(), request.getStartTime(), request.getEndTime());

        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setHall(hall);

        if (request.getSpeakerId() != null) {
            User speaker = userRepository.findById(request.getSpeakerId())
                    .orElseThrow(() -> new RuntimeException("Speaker not found"));
            session.setSpeaker(speaker);
        } else {
            session.setSpeaker(null);
        }

        Session saved = sessionRepository.save(session);
        return mapToResponse(saved);
    }

    public void deleteSession(Long userId, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (userId != session.getEvent().getOrganizerId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only event organizers can add sessions!");
        }
        sessionRepository.deleteById(sessionId);
    }

    // map session to response
    private SessionResponse mapToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setHallId(session.getHall().getId());
        if (session.getSpeaker() != null) {
            response.setSpeakerId(session.getSpeaker().getId());
            response.setSpeakerName(session.getSpeaker().getName());
        }
        return response;
    }

    private void validateSessionTimes(Event event, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate eventDate = event.getEventDate().toLocalDate();

        if (!startTime.toLocalDate().equals(eventDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Session start time must be on the same date as the event (" + eventDate + ")");
        }

        if (!endTime.toLocalDate().equals(eventDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Session end time must be on the same date as the event (" + eventDate + ")");
        }

        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Session end time must be after the start time");
        }

        if (eventDate.isEqual(LocalDate.now()) && startTime.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Session start time cannot be in the past");
        }
    }
}