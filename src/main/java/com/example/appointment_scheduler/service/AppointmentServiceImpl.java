package com.example.appointment_scheduler.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    
   


    @Autowired
    private AppointmentRepository appointmentRepository;
    

    @Override
    public Appointment saveAppointment(Appointment app) {
        return appointmentRepository.save(app);
    }


    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    

}
