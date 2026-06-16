package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Events.CreateEventRequest;
import fmi.eventmanager.Agendy.model.dto.Events.EventResponse;
import fmi.eventmanager.Agendy.model.dto.Events.UpdateEventRequest;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.EventStatus;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventResponse createEvent(CreateEventRequest request, Long organizerId) {

        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can add events!");
        }

        Event event = new Event(
                organizerId,
                request.getTitle(),
                request.getDescription(),
                request.getVenue(),
                request.getEventDate(),
                request.getCapacity(),
                EventStatus.PAST
        );
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventResponse> responses = new ArrayList<>();
        for (Event e : events) {
            responses.add(mapToResponse(e));
        }
        return responses;
    }

    public EventResponse updateEvent(Long id, UpdateEventRequest request, Long userId) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the organizer of this event");
        }

        event.setTitle(request.getTitle());
        event.setVenue(request.getVenue());
        event.setEventDate(request.getEventDate());
        event.setCapacity(request.getCapacity());
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    public void deleteEvent(Long id, Long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the organizer of this event");
        }

        eventRepository.deleteById(id);
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setVenue(event.getVenue());
        response.setEventDate(event.getEventDate());
        response.setCapacity(event.getCapacity());
        response.setStatus(event.getStatus().name());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }
}