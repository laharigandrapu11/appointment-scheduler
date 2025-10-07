package com.example.appointment_scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUsers() {

        return userRepository.findAll();

    }


    @Override
    public User fetchUserByEmail(String emailId) {

        return userRepository.findByEmail(emailId).get();
    }
    
}
