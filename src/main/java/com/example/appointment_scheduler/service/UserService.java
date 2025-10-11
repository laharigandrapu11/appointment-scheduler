package com.example.appointment_scheduler.service;


import com.example.appointment_scheduler.error.IncorrectPasswordException;
import com.example.appointment_scheduler.error.UserAlreadyExistsException;
import com.example.appointment_scheduler.error.UserNotFoundException;
import com.example.appointment_scheduler.model.User;


public interface UserService {

    public User saveUser(User user) throws UserAlreadyExistsException;

    public User validateUser(String email, String password) throws IncorrectPasswordException, UserNotFoundException;
    
}
