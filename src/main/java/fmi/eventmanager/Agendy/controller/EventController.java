package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.Events.CreateEventRequest;
import fmi.eventmanager.Agendy.model.dto.Events.EventResponse;
import fmi.eventmanager.Agendy.model.dto.Events.UpdateEventRequest;
import fmi.eventmanager.Agendy.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> responses = eventService.getAllEvents();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid CreateEventRequest request,
                                                     @AuthenticationPrincipal Long userId) {
        EventResponse response = eventService.createEvent(request, userId);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId) {
        EventResponse response = eventService.getEventById(eventId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,
                                                     @RequestBody @Valid UpdateEventRequest request,
                                                     @AuthenticationPrincipal Long userId) {
        //trqbva da se podade id da se vidi da li sum sobstvenik
        EventResponse response = eventService.updateEvent(eventId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId,
                                            @AuthenticationPrincipal Long userId) {
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.noContent().build();
    }
}
