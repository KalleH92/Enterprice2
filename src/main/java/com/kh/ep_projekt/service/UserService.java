package com.kh.ep_projekt.service;

import com.kh.ep_projekt.model.User;
import com.kh.ep_projekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        return userRepository.findByEmail(email).map(user -> {
            System.out.println("Inmatat lösenord: " + password);
            System.out.println("Hash i databasen: " + user.getPasswordHash());
            boolean match = passwordEncoder.matches(password, user.getPasswordHash());
            System.out.println("Matchning lyckades: " + match);
            return match;
        }).orElse(false);
    }

}
