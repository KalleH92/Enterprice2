package com.kh.ep_projekt.controller;

import com.kh.ep_projekt.model.User;
import com.kh.ep_projekt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String password) {
        try {
            User newUser = userService.registerUser(name, email, password);
            return ResponseEntity.status(201).body("User registered with ID: " + newUser.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        boolean authenticated = userService.authenticateUser(email, password);
        if (authenticated) {
            return ResponseEntity.ok("Login succesful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
