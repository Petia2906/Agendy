package fmi.eventmanager.Agendy.model.dto;

import fmi.eventmanager.Agendy.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private static final int MIN_NAME_SIZE = 2;
    private static final int MAX_NAME_SIZE = 50;
    private static final int MIN_PASSWORD_SIZE = 6;


    @NotBlank(message = "Name is required")
    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE,
            message = "Name must be between 2 and 50 characters")
    public String name;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    public String email;

    @Size(min = MIN_PASSWORD_SIZE, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    public String password;

    @NotNull(message = "Role is required")
    public Role role;
}
