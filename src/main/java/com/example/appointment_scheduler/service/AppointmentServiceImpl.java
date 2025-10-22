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
        String groupId = java.util.UUID.randomUUID().toString();
        
        if(app.getDaySchedule() != null && app.getScheduleType().equalsIgnoreCase("single")){
            appointments = createSingleDayAppointments(app, groupId);
        } else if (app.getDaySchedule() != null && app.getScheduleType().equalsIgnoreCase("multiple")) {
            appointments = createMultipleDaysAppointments(app, groupId);
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
    
    private List<Appointment> createSingleDayAppointments(Appointment app, String groupId) {
        List<Appointment> appointments = new ArrayList<>();
        LocalTime currentStartTime = app.getStartTime();
        LocalTime endTime = app.getEndTime();
        int duration = app.getDurationMinutes();
        int gap = app.getGapMinutes() != null ? app.getGapMinutes() : 0;
        
        while (currentStartTime.plusMinutes(duration).isBefore(endTime) || currentStartTime.plusMinutes(duration).equals(endTime)) {
            Appointment newApp = new Appointment();
            newApp.setTitle(app.getTitle());
            newApp.setDescription(app.getDescription());
            newApp.setLocation(app.getLocation());
            newApp.setAppointmentType(app.getAppointmentType());
            newApp.setDate(app.getDate());
            newApp.setStartTime(currentStartTime);
            newApp.setEndTime(currentStartTime.plusMinutes(duration));
            newApp.setDurationMinutes(duration);
            newApp.setGapMinutes(gap);
            newApp.setGroupId(groupId);
            newApp.setScheduleType("single");
            appointments.add(appointmentRepository.save(newApp));
            
            currentStartTime = addMinutesToTime(currentStartTime.plusMinutes(duration), gap);
        }
        
        return appointments;
    }

    private List<Appointment> createMultipleDaysAppointments(Appointment app, String groupId) {
        List<Appointment> appointments = new ArrayList<>();
        
        for (var daySchedule : app.getDaySchedule()) {
            LocalTime currentStartTime = daySchedule.getStartTime();
            LocalTime endTime = daySchedule.getEndTime();
            int duration = app.getDurationMinutes();
            int gap = app.getGapMinutes() != null ? app.getGapMinutes() : 0;
            
            while (currentStartTime.plusMinutes(duration).isBefore(endTime) || currentStartTime.plusMinutes(duration).equals(endTime)) {
                Appointment newApp = new Appointment();
                newApp.setTitle(app.getTitle());
                newApp.setDescription(app.getDescription());
                newApp.setLocation(app.getLocation());
                newApp.setAppointmentType(app.getAppointmentType());
                newApp.setDate(daySchedule.getDate());
                newApp.setStartTime(currentStartTime);
                newApp.setEndTime(currentStartTime.plusMinutes(duration));
                newApp.setDurationMinutes(duration);
                newApp.setGapMinutes(gap);
                newApp.setGroupId(groupId);
                newApp.setScheduleType("multiple");
                appointments.add(appointmentRepository.save(newApp));
                
                currentStartTime = addMinutesToTime(currentStartTime.plusMinutes(duration), gap);
            }
        }
        
        return appointments;
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
