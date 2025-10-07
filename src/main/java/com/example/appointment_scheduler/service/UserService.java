package com.example.appointment_scheduler.service;


import java.util.List;

import com.example.appointment_scheduler.model.User;


public interface UserService {

    public User saveUser(User user);

    public List<User> fetchUsers();

    public User fetchUserByEmail(String emailId);
    
}
