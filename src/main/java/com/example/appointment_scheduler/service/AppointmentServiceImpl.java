package com.example.appointment_scheduler.service;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appointment_scheduler.error.AppointmentAlreadyBooked;
import com.example.appointment_scheduler.error.AppointmentNotFoundException;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    
   


    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    
    @Override
    public List<Appointment> createAppointments(Appointment app) {
        List<Appointment> appointments = new ArrayList<>();
        
        LocalTime currentStartTime = app.getStartTime();
        int appointmentNumber = 1;
        
        while (true) {
            LocalTime appointmentEndTime = addMinutesToTime(currentStartTime, app.getDurationMinutes());
            if (appointmentEndTime.isAfter(app.getEndTime())) {
                break; 
            }

            Appointment appointment = new Appointment();
            appointment.setTitle(app.getTitle() + " - Slot " + appointmentNumber);
            appointment.setDescription(app.getDescription());
            appointment.setAppointmentType(app.getAppointmentType());
            appointment.setDate(app.getDate());
            appointment.setStartTime(currentStartTime);
            appointment.setEndTime(appointmentEndTime);
            appointment.setLocation(app.getLocation());
            appointment.setDurationMinutes(app.getDurationMinutes());
            appointment.setGapMinutes(app.getGapMinutes());
            
            appointments.add(appointmentRepository.save(appointment));
            
            int totalMinutes = app.getDurationMinutes() + app.getGapMinutes();
            currentStartTime = addMinutesToTime(currentStartTime, totalMinutes);
            appointmentNumber++;
        }
        
        return appointments;
    }
    
    private LocalTime addMinutesToTime(LocalTime time, int minutesToAdd) {
        int totalMinutes = time.getHour() * 60 + time.getMinute();
        totalMinutes = totalMinutes + minutesToAdd;
        
        int newHour = totalMinutes / 60;
        int newMinute = totalMinutes % 60;
        
        if (newHour >= 24) {
            newHour = newHour % 24;
        }
        
        return LocalTime.of(newHour, newMinute);
    }
    
    @Override
    public Appointment bookAppointment(int appointmentId, User user) throws AppointmentAlreadyBooked, AppointmentNotFoundException {
        
        if (hasUserBookedAppointment(user)) {
            throw new AppointmentAlreadyBooked("You have already booked an appointment");
        }
        
        Optional<Appointment> appointmentOptions = appointmentRepository.findById(appointmentId);
        if (!appointmentOptions.isPresent()) {
            throw new AppointmentNotFoundException("Appointment not found!");
        }
        
        Appointment appointment = appointmentOptions.get();
        if (appointment.isBooked()) {
            throw new AppointmentAlreadyBooked("Appointment is already booked");
        }
        
        appointment.setBooked(true);
        appointment.setBookedBy(user);
        return appointmentRepository.save(appointment);
    }
    
    @Override
    public boolean hasUserBookedAppointment(User user) {
        return appointmentRepository.findByBookedBy(user).isPresent();
    }

}
