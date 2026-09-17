package fmi.eventmanager.Agendy.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "speakers")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Speaker extends User {
    private static final int ORGANIZATION_NAME_LENGTH = 100;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(length = ORGANIZATION_NAME_LENGTH)
    private String organization;

    public Speaker() {
        this.setRole(Role.SPEAKER);
    }

}