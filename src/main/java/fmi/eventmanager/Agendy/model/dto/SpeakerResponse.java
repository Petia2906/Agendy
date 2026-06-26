package fmi.eventmanager.Agendy.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeakerResponse {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String photoUrl;
    private String organization;
}