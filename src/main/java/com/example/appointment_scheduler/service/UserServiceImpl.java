package com.example.appointment_scheduler.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appointment_scheduler.error.IncorrectPasswordException;
import com.example.appointment_scheduler.error.UserAlreadyExistsException;
import com.example.appointment_scheduler.error.UserNotFoundException;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with {user.getEmail()} email already exists, try logging in!");
        }
        return userRepository.save(user);
    }

    @Override
    public User validateUser(String email, String password) throws IncorrectPasswordException, UserNotFoundException {
        Optional<User> user =  userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new UserNotFoundException("User Not Available!");
        }
        else if(!user.get().getPassword().equals(password)){
            throw new IncorrectPasswordException("Incorrect Password!");
        }
        return user.get();
    }
    
}
