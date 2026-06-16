package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Halls.CreateHallRequest;
import fmi.eventmanager.Agendy.model.dto.Halls.HallResponse;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Hall;
import fmi.eventmanager.Agendy.model.entity.Role;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.HallRepository;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public HallService(HallRepository hallRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public HallResponse createHall(Long userId, Long eventId, CreateHallRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        if (userId != event.getOrganizerId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only organizers can add halls.");
        }
        Hall hall = new Hall(event, request.getName(), request.getCapacity());
        return mapToResponse(hallRepository.save(hall));
    }

    public List<HallResponse> getHallsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Hall> halls = hallRepository.findByEvent(event);
        List<HallResponse> responses = new java.util.ArrayList<>();

        for (Hall h : halls) {
            responses.add(mapToResponse(h));
        }
        return responses;
    }

    public void deleteHall(Long userId, Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hall not found!"));
        if (hall.getEvent().getOrganizerId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only organizers of events can delete rooms");
        }
        hallRepository.deleteById(hallId);
    }

    private HallResponse mapToResponse(Hall hall) {
        HallResponse response = new HallResponse();
        response.setId(hall.getId());
        response.setName(hall.getName());
        response.setCapacity(hall.getCapacity());
        return response;
    }
}
