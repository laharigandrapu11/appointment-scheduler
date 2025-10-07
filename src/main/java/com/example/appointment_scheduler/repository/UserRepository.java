package com.example.appointment_scheduler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.appointment_scheduler.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //save, delete, findAll are provided by JpaRepository
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
}
