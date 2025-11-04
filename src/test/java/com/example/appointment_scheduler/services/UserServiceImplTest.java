package com.example.appointment_scheduler.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.appointment_scheduler.error.IncorrectPasswordException;
import com.example.appointment_scheduler.error.UserAlreadyExistsException;
import com.example.appointment_scheduler.error.UserNotFoundException;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.UserRepository;
import com.example.appointment_scheduler.service.UserServiceImpl;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateUser() throws IncorrectPasswordException, UserNotFoundException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User validatedUser = userServiceImpl.validateUser(user.getEmail(), user.getPassword());

        assertNotNull(validatedUser);
        assertEquals(user.getEmail(), validatedUser.getEmail());
        assertEquals(user.getPassword(), validatedUser.getPassword());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testWrongPassword() throws IncorrectPasswordException, UserNotFoundException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IncorrectPasswordException.class, () -> {
            userServiceImpl.validateUser(user.getEmail(), "wrongpassword");
        });
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testSaveUser() throws UserAlreadyExistsException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.save(user)).thenReturn(user);

        User saved = userServiceImpl.saveUser(user);
        assertNotNull(saved);
        assertEquals(user.getEmail(), saved.getEmail());
        assertEquals(user.getPassword(), saved.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}