package com.example.appointment_scheduler.repository;
import com.example.appointment_scheduler.model.Appointment;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    //save, delete, findAll are provided by JpaRepository
    Optional<Appointment> findById(int id);
    

    
} 






