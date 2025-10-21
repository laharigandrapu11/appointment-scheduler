package com.example.appointment_scheduler.error;

public class AppointmentNotFoundException extends Exception{
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    
}