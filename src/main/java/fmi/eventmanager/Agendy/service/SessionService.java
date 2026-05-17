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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository, HallRepository hallRepository,
                          UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.userRepository = userRepository;
    }

    // create session for event
    public SessionResponse createSession(Long eventId, CreateSessionRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

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

        List<Session> sessions = sessionRepository.findByEvent(event);
        List<SessionResponse> responses = new ArrayList<>();
        for (Session s : sessions) {
            responses.add(mapToResponse(s));
        }
        return responses;
    }

    // update session
    public SessionResponse updateSession(Long sessionId, UpdateSessionRequest request) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));


        session.setTitle(request.getTitle());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setHall(hall);

        Session saved = sessionRepository.save(session);
        return mapToResponse(saved);
    }

    public void deleteSession(Long sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
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
        }
        return response;
    }
}