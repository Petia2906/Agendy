package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.CreateSpeakerRequest;
import fmi.eventmanager.Agendy.model.dto.SpeakerResponse;
import fmi.eventmanager.Agendy.model.dto.UpdateSpeakerRequest;
import fmi.eventmanager.Agendy.model.entity.Speaker;
import fmi.eventmanager.Agendy.service.SpeakerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @PostMapping
    public ResponseEntity<?> createSpeaker(@RequestBody @Valid CreateSpeakerRequest request,
                                           @AuthenticationPrincipal Long userId) {
        try {
            Speaker created = speakerService.createSpeaker(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SpeakerResponse>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }

    @PutMapping("/{speakerId}")
    public ResponseEntity<?> updateSpeaker(@PathVariable Long speakerId,
                                           @RequestBody @Valid UpdateSpeakerRequest request,
                                           @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(speakerService.updateSpeaker(userId, speakerId, request));
    }
}
