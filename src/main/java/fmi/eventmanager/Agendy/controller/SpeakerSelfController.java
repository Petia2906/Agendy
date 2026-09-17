package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.Events.EventResponse;
import fmi.eventmanager.Agendy.service.SpeakerSelfService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/speakers")
public class SpeakerSelfController {

    private final SpeakerSelfService speakerSelfService;

    public SpeakerSelfController(SpeakerSelfService speakerSelfService) {
        this.speakerSelfService = speakerSelfService;
    }

    @GetMapping("/me/events")
    public ResponseEntity<List<EventResponse>> getMyEvents(@AuthenticationPrincipal Long userId) {
        List<EventResponse> events = speakerSelfService.getEventsForSpeaker(userId);
        return ResponseEntity.ok(events);
    }
}