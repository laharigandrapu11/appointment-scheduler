package com.example.appointment_scheduler.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

import com.example.appointment_scheduler.error.IncorrectPasswordException;
import com.example.appointment_scheduler.error.UserAlreadyExistsException;
import com.example.appointment_scheduler.error.UserNotFoundException;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.model.Login;
import com.example.appointment_scheduler.service.UserService;
import com.example.appointment_scheduler.service.AppointmentService;

import jakarta.validation.Valid;

@RestController

public class UserController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // Constructor injection to provide the services
    public UserController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/users/register")
    //converting the request from json to user object using RequestBody
    public User createUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        LOGGER.info("Creating user with email: {}", user.getEmail());
        return userService.saveUser(user);
        
    }

    @PostMapping("/users/login")
    //converting the request from json to Login object using RequestBody
    public User validateUser(@Valid @RequestBody Login login, HttpSession session) throws IncorrectPasswordException, UserNotFoundException {
        LOGGER.info("Logging in user with email: {}", login.getEmail());
        User user = userService.validateUser(login.getEmail(), login.getPassword());
        session.setAttribute("user", user);
        return user;
    }


    @GetMapping("/appointments")
    public List<Appointment> getAppointments() {
        LOGGER.info("Fetching all appointments");
        return appointmentService.findAll();
    }


}
