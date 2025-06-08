package com.kh.ep_projekt;

import com.kh.ep_projekt.service.UserService;
import com.kh.ep_projekt.model.User;
import com.kh.ep_projekt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void registerUser_shouldCreateUserWhenEmailIsUnique() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashed");

        User result = userService.registerUser("Test", "test@example.com", "password");

        assertEquals("Test", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("hashed", result.getPasswordHash());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerUser_shouldFailWhenEmailExists() {
        when(userRepository.existsByEmail("exist@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userService.registerUser("Test", "exist@example.com", "password")
        );
    }

    @Test
    public void authenticateUser_shouldReturnTrueForCorrectPassword() {
        User user = new User();
        user.setPasswordHash("hashed");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        assertTrue(userService.authenticateUser("user@example.com", "password"));
    }

    @Test
    public void authenticateUser_shouldReturnFalseForIncorrectPassword() {
        User user = new User();
        user.setPasswordHash("hashed");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertFalse(userService.authenticateUser("user@example.com", "wrong"));
    }

    @Test
    public void authenticateUser_shouldReturnFalseIfUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertFalse(userService.authenticateUser("unknown@example.com", "password"));
    }
}
