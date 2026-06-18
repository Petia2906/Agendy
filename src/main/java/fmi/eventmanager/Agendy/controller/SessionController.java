package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.Sessions.CreateSessionRequest;
import fmi.eventmanager.Agendy.model.dto.Sessions.SessionResponse;
import fmi.eventmanager.Agendy.model.dto.Sessions.UpdateSessionRequest;
import fmi.eventmanager.Agendy.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/events/{eventId}/sessions")
    public ResponseEntity<SessionResponse> createSession(@PathVariable Long eventId, @RequestBody @Valid CreateSessionRequest request) {
        SessionResponse response = sessionService.createSession(eventId, request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/events/{eventId}/sessions")
    public ResponseEntity<List<SessionResponse>> getSessionsByEvent(@PathVariable Long eventId) {
        List<SessionResponse> responses = sessionService.getSessionsByEvent(eventId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionResponse> updateSession(@PathVariable Long sessionId, @RequestBody @Valid UpdateSessionRequest request) {
        SessionResponse response = sessionService.updateSession(sessionId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}