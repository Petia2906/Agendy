package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.entity.Speaker;
import fmi.eventmanager.Agendy.service.SpeakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @PostMapping
    public ResponseEntity<?> createSpeaker(@RequestBody Speaker speaker, @AuthenticationPrincipal Long userId) {
        try {
            Speaker created = speakerService.createSpeaker(userId, speaker);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
