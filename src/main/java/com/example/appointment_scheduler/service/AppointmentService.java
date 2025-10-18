package com.example.appointment_scheduler.service;
import java.util.List;

import com.example.appointment_scheduler.model.Appointment;

public interface AppointmentService {
   
   public List<Appointment> findAll();
   public List<Appointment> createAppointments(Appointment appointment);
    
}

