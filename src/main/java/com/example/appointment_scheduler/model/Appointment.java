package com.example.appointment_scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Title is required!!")
    private String title;
    
    @NotBlank(message = "Description is required!!")
    private String description;
    
    @NotBlank(message = "Location is required!!")
    private String location;
    
    @NotBlank(message = "Appointment type is required!!")
    private String appointmentType;
    
    @NotNull(message = "Date is required!!")
    private LocalDate date;
    
    @NotNull(message = "Start time is required!!")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required!!")
    private LocalTime endTime;
    
    @NotNull(message = "Duration is required!!")
    private Integer durationMinutes;

    private Integer gapMinutes;

    private boolean isBooked = false;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User bookedBy;
    
   @AssertTrue(message = "End time must be after start time")
    public boolean validateStartandEnd() {
        if (startTime == null || endTime == null) {
            return false;
        }
        if (endTime.isAfter(startTime)) {
            return true;
        }
        return false;
    }
    
    @AssertTrue(message = "Not enough time in the selected time range for the appointment duration")
    public boolean validateTimerange() {
        if (startTime == null || endTime == null || durationMinutes == null || gapMinutes == null) {
            return true;
        }
        
        long totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        
        if (totalMinutes >= durationMinutes) {
            return true;
        } else {
            return false;
        }
    }
}
