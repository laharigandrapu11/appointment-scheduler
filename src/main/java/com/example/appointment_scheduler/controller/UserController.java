package com.example.appointment_scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.appointment_scheduler.error.IncorrectPasswordException;
import com.example.appointment_scheduler.error.UserAlreadyExistsException;
import com.example.appointment_scheduler.error.UserNotFoundException;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.model.Login;
import com.example.appointment_scheduler.service.UserService;

import jakarta.validation.Valid;

@RestController

public class UserController {

    @Autowired //why only this way?
    private UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/users/register")
    //converting the request from json to user object using RequestBody
    public User createUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        LOGGER.info("Creating user with email: {}", user.getEmail());
        return userService.saveUser(user);
        
    }

    @PostMapping("/users/login")
    //converting the request from json to LoginRequest object using RequestBody
    public User validateUser(@Valid @RequestBody Login login) throws IncorrectPasswordException, UserNotFoundException {
        LOGGER.info("Logging in user with email: {}", login.getEmail());
        return userService.validateUser(login.getEmail(), login.getPassword());
        
    }
}
