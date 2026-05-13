package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.dto.LoginRequest;
import fmi.eventmanager.Agendy.model.dto.RegisterRequest;
import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!isPasswordMatch) {
            throw new RuntimeException("Invalid password!");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
