package com.example.appointment_scheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

//dto for multiple appointment days
@Data
@NoArgsConstructor
public class DaySchedule {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    
}