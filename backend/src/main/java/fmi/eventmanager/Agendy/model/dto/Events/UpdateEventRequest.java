package fmi.eventmanager.Agendy.model.dto.Events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateEventRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String venue;

    @NotNull
    private LocalDateTime eventDate;

    @Min(1)
    private int capacity;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
