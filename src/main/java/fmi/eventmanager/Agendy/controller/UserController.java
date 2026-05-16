package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
