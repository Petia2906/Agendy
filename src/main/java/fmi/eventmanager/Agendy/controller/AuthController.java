package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.LoginRequest;
import fmi.eventmanager.Agendy.model.dto.RegisterRequest;
import fmi.eventmanager.Agendy.model.entity.User;
git import fmi.eventmanager.Agendy.service.JwtService;
import fmi.eventmanager.Agendy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            String token = jwtService.generateToken(registeredUser.getId(), registeredUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Registered successfully",
                    "token", token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request);
            String token = jwtService.generateToken(user.getId(), user.getEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful! Welcome, " + user.getName(),
                    "token", token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }
}
