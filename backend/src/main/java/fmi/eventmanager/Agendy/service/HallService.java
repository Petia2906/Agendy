package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.Halls.CreateHallRequest;
import fmi.eventmanager.Agendy.model.dto.Halls.HallResponse;
import fmi.eventmanager.Agendy.model.entity.Event;
import fmi.eventmanager.Agendy.model.entity.Hall;
import fmi.eventmanager.Agendy.repository.EventRepository;
import fmi.eventmanager.Agendy.repository.HallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final EventRepository eventRepository;

    public HallService(HallRepository hallRepository, EventRepository eventRepository) {
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
    }

    public HallResponse createHall(Long eventId, CreateHallRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
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

    public void deleteHall(Long hallId) {
        hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found"));
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
