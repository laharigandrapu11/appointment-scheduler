package com.example.appointment_scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.service.UserService;

import jakarta.validation.Valid;

@RestController

public class UserController {

    @Autowired //why only this way?
    private UserService userService;

    @PostMapping("/users")
    //converting the request from json to user object using RequestBody
    public User createUser(@Valid @RequestBody User user) {
        
        
        return userService.saveUser(user);
        
    }

    @GetMapping("/users")
    public List<User> fetchUsers() {

        return userService.fetchUsers();

    }

    @GetMapping("/users/{email}")
    public User fetchUserByEmail(@PathVariable("email") String emailId) {

        return userService.fetchUserByEmail(emailId);

    }
}
