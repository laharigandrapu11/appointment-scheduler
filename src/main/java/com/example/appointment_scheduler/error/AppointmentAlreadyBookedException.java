package com.example.appointment_scheduler.error;

public class AppointmentAlreadyBookedException extends Exception{
    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }
    
}