package fmi.eventmanager.Agendy.model.dto.Halls;

public class HallResponse {
    private Long id;
    private String name;
    private int capacity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}