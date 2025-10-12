package com.example.appointment_scheduler.service;
import java.util.List;

import com.example.appointment_scheduler.model.Appointment;

public interface AppointmentService {
    
   public Appointment saveAppointment(Appointment app);
   public List<Appointment> findAll();
    
}

