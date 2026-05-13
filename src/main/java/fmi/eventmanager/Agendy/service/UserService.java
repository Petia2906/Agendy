package fmi.eventmanager.Agendy.service;

import fmi.eventmanager.Agendy.model.entity.User;
import fmi.eventmanager.Agendy.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
