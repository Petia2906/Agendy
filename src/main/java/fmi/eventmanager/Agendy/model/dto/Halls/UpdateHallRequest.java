package fmi.eventmanager.Agendy.model.dto.Halls;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateHallRequest {
    @NotBlank
    private String name;

    @Min(1)
    private int capacity;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
