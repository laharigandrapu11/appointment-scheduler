package com.example.appointment_scheduler.service;
import java.util.List;

import com.example.appointment_scheduler.error.AppointmentAlreadyBookedException;
import com.example.appointment_scheduler.error.CancelAppointmentException;
import com.example.appointment_scheduler.error.AppointmentNotFoundException;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.model.User;

public interface AppointmentService {
   
   public List<Appointment> findAll();
   public List<Appointment> createAppointments(Appointment appointment);
   public Appointment bookAppointment(int appointmentId, User user) throws AppointmentNotFoundException, AppointmentAlreadyBookedException;
   public boolean hasUserBookedAppointment(User user);
   public void cancelAppointment(int appointmentId, User user) throws AppointmentNotFoundException, CancelAppointmentException;
   public void saveAppointment(Appointment appointment);
   public boolean isGroupBooked(String groupId);
   public void updateAppointmentGroup(String groupId, String title, String description, String location);
   public Appointment findFirstAppointmentByGroupId(String groupId);
   public void deleteAppointmentGroup(String groupId);
    
}

