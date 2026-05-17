package fmi.eventmanager.Agendy.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hall")
@Getter
@Setter
public class Hall {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    protected Hall() {}

    public Hall(Event event, String name, int capacity) {
        this.event = event;
        this.name = name;
        this.capacity = capacity;
    }
}
