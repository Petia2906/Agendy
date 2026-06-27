package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Events.CreateEventRequest;
import fmi.eventmanager.Agendy.model.dto.Events.EventResponse;
import fmi.eventmanager.Agendy.model.dto.Events.UpdateEventRequest;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.EventStatus;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.FeedbackRepository;
import fmi.eventmanager.Agendy.repository.HallRepository;
import fmi.eventmanager.Agendy.repository.SessionRepository;
import fmi.eventmanager.Agendy.repository.TicketRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final HallRepository hallRepository;
    private final TicketRepository ticketRepository;
    private final FeedbackRepository feedbackRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository,
                        SessionRepository sessionRepository, HallRepository hallRepository,
                        TicketRepository ticketRepository, FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.hallRepository = hallRepository;
        this.ticketRepository = ticketRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public EventResponse createEvent(CreateEventRequest request, Long organizerId) {
        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can add events!");
        }

        if (request.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event date cannot be in the past");
        }

        Event event = new Event(
                organizerId,
                request.getTitle(),
                request.getDescription(),
                request.getVenue(),
                request.getEventDate(),
                request.getCapacity(),
                request.getPrice()
                //EventStatus.PAST
        );
        event.setStatus(computeStatus(request.getEventDate()));
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

    public List<EventResponse> getMyEvents(Long organizerId) {
        List<EventResponse> responses = new ArrayList<>();
        for (Event e : eventRepository.findByOrganizerId(organizerId)) {
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

        if (request.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event date cannot be in the past");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setEventDate(request.getEventDate());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());
        event.setStatus(computeStatus(request.getEventDate()));
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    //must refactor logic later
    @Transactional
    public void deleteEvent(Long id, Long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the organizer of this event");
        }

        sessionRepository.deleteAll(sessionRepository.findByEvent(event));
        hallRepository.deleteAll(hallRepository.findByEvent(event));
        ticketRepository.deleteAll(ticketRepository.findByEventId(event.getId()));
        feedbackRepository.deleteAll(feedbackRepository.findByEventId(event.getId()));
        eventRepository.deleteById(id);
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setOrganizerId(event.getOrganizerId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setVenue(event.getVenue());
        response.setEventDate(event.getEventDate());
        response.setCapacity(event.getCapacity());
        response.setPrice(event.getPrice());
        response.setStatus(computeStatus(event.getEventDate()).name());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }

    private EventStatus computeStatus(LocalDateTime eventDate) {
        LocalDate eventDay = eventDate.toLocalDate();
        LocalDate today = LocalDate.now();

        if (eventDay.isBefore(today)) return EventStatus.PAST;
        if (eventDay.isEqual(today)) return EventStatus.TODAY;
        return EventStatus.UPCOMING;
    }
}