package com.example.appointment_scheduler.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.appointment_scheduler.error.AppointmentAlreadyBookedException;
import com.example.appointment_scheduler.error.AppointmentNotFoundException;
import com.example.appointment_scheduler.error.CancelAppointmentException;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.model.DaySchedule;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.AppointmentRepository;
import com.example.appointment_scheduler.repository.UserRepository;
import com.example.appointment_scheduler.service.AppointmentServiceImpl;

public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookAppointment_SuccessfullyBooked() throws AppointmentAlreadyBookedException, AppointmentNotFoundException {
        
        User student = new User();
        student.setId(1);
        student.setEmail("student@example.com");
        student.setRole("STUDENT");
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Test Appointment");
        appointment.setAppointmentType("Individual");
        appointment.setBooked(false);
        appointment.setGroupId("group123");
        
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.findByBookedBy(student)).thenReturn(new ArrayList<>());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment bookedAppointment = appointmentServiceImpl.bookAppointment(1, student);

        assertNotNull(bookedAppointment);
        assertTrue(bookedAppointment.isBooked());
        assertEquals(student, bookedAppointment.getBookedBy());
        verify(appointmentRepository, times(1)).findById(1);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testBookAppointment_AlreadyBooked() throws AppointmentAlreadyBookedException, AppointmentNotFoundException {
        User student = new User();
        student.setId(1);
        student.setEmail("student@example.com");
        student.setRole("STUDENT");
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Test Appointment");
        appointment.setAppointmentType("Individual");
        appointment.setBooked(true);

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

        AppointmentAlreadyBookedException exception = assertThrows(AppointmentAlreadyBookedException.class, () -> {
            appointmentServiceImpl.bookAppointment(1, student);
        });
        assertEquals("Appointment is already booked", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(1);
        verify(appointmentRepository, times(0)).save(any(Appointment.class));
    }

    @Test
    public void testCreateAppointments_Multiple() throws AppointmentAlreadyBookedException, AppointmentNotFoundException {
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Test Appointment");
        appointment.setAppointmentType("Group");
        appointment.setDurationMinutes(30);
        appointment.setGapMinutes(0);
        appointment.setDate(LocalDate.now());
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setEndTime(LocalTime.of(11, 0));
        
        List<DaySchedule> daySchedules = new ArrayList<>();
        DaySchedule daySchedule1 = new DaySchedule();
        daySchedule1.setDate(LocalDate.now());
        daySchedule1.setStartTime(LocalTime.of(14, 0));
        daySchedule1.setEndTime(LocalTime.of(15, 0));

        DaySchedule daySchedule2 = new DaySchedule();
        daySchedule2.setDate(LocalDate.now().plusDays(1));
        daySchedule2.setStartTime(LocalTime.of(16, 0));
        daySchedule2.setEndTime(LocalTime.of(17, 0));

        daySchedules.add(daySchedule1);
        daySchedules.add(daySchedule2);
        appointment.setDaySchedule(daySchedules);

        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        List<Appointment> appointments = appointmentServiceImpl.createAppointments(appointment);
        assertNotNull(appointments);
        assertEquals(6, appointments.size()); 
        verify(appointmentRepository, times(6)).save(any(Appointment.class));

    }


    @Test
    public void viewAllAppointments() throws AppointmentNotFoundException {

        List<Appointment> appointments = new ArrayList<>();
        Appointment apt1 = new Appointment();
        Appointment apt2 = new Appointment();
        apt1.setId(1);
        apt1.setTitle("Test Appointment 1");
        apt1.setAppointmentType("Individual");
        apt1.setBooked(false);
        apt1.setGroupId("group123");
        appointments.add(apt1);

        apt2.setId(2);
        apt2.setTitle("Test Appointment 2");
        apt2.setAppointmentType("Individual");
        apt2.setBooked(false);
        apt2.setGroupId("group123");
        appointments.add(apt2);

        when(appointmentRepository.findAll()).thenReturn(appointments);
        List<Appointment> allAppointments = appointmentServiceImpl.findAll();
        assertNotNull(allAppointments);
        assertEquals(2, allAppointments.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void cancelAppointment_professor() throws AppointmentNotFoundException, CancelAppointmentException {
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Test Appointment");
        appointment.setAppointmentType("Individual");
        appointment.setBooked(true);
        appointment.setGroupId("group123");

        User professor = new User();
        professor.setId(1);
        professor.setEmail("professor@example.com");
        professor.setRole("PROFESSOR");

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        appointmentServiceImpl.cancelAppointment(1, professor);
        verify(appointmentRepository, times(1)).findById(1);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void cancelAppointment_student_failed() throws AppointmentNotFoundException, CancelAppointmentException {
        User student = new User();
        student.setId(1);
        student.setEmail("student@example.com");
        student.setRole("STUDENT");
        Appointment appointment = new Appointment();
        appointment.setId(1);
        appointment.setTitle("Test Appointment");
        appointment.setAppointmentType("Individual");
        appointment.setBooked(true);
        appointment.setGroupId("group123");
        appointment.setDate(LocalDate.now().minusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setBookedBy(student);
        
       

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.findByGroupIdOrderByDateAscStartTimeAsc("group123")).thenReturn(List.of(appointment));
        CancelAppointmentException exception = assertThrows(CancelAppointmentException.class, () -> {
            appointmentServiceImpl.cancelAppointment(1, student);
        });
        assertTrue(exception.getMessage().contains("You can only cancel before the first appointment in the group"));
        verify(appointmentRepository, times(1)).findById(1);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }


}