package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.Halls.CreateHallRequest;
import fmi.eventmanager.Agendy.model.dto.Halls.HallResponse;
import fmi.eventmanager.Agendy.service.HallService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/halls")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping
    public ResponseEntity<HallResponse> createHall(@PathVariable Long eventId, @RequestBody @Valid CreateHallRequest request) {
        HallResponse response = hallService.createHall(eventId, request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HallResponse>> getHallsByEvent(@PathVariable Long eventId) {
        List<HallResponse> responses = hallService.getHallsByEvent(eventId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{hallId}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long hallId) {
        hallService.deleteHall(hallId);
        return ResponseEntity.noContent().build();
    }
}