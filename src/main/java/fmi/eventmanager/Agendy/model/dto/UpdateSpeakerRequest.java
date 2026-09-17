package fmi.eventmanager.Agendy.model.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateSpeakerRequest {
    @NotBlank
    private String name;
    private String bio;
    private String photoUrl;
    private String organization;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
}
