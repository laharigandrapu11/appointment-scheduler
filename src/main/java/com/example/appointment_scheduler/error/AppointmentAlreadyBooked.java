package com.example.appointment_scheduler.error;

public class AppointmentAlreadyBooked extends Exception{
    public AppointmentAlreadyBooked(String message) {
        super(message);
    }
    
}