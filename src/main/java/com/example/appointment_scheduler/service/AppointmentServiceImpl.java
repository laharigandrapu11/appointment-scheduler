package com.example.appointment_scheduler.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.appointment_scheduler.error.AppointmentAlreadyBookedException;
import com.example.appointment_scheduler.error.CancelAppointmentException;
import com.example.appointment_scheduler.error.AppointmentNotFoundException;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.model.DaySchedule;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.AppointmentRepository;
import com.example.appointment_scheduler.repository.UserRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    
   


    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    
    @Override
    public List<Appointment> createAppointments(Appointment app) {
        List<Appointment> appointments = new ArrayList<>();
        String appGroupId = java.util.UUID.randomUUID().toString();
        List<DaySchedule> validDaySchedules = new ArrayList<>();
        
        if (app.getDaySchedule() != null) {
            for (DaySchedule daySchedule : app.getDaySchedule()) {
                if (daySchedule != null && 
                    daySchedule.getDate() != null && 
                    daySchedule.getStartTime() != null && 
                    daySchedule.getEndTime() != null) {
                    
                    validDaySchedules.add(daySchedule);
                }
            }
        }
        
        if (validDaySchedules.isEmpty()) {
            appointments = createSingleDayAppointments(app, appGroupId);
        } else {
            DaySchedule firstDay = new DaySchedule();
            firstDay.setDate(app.getDate());
            firstDay.setStartTime(app.getStartTime());
            firstDay.setEndTime(app.getEndTime());
            
            validDaySchedules.add(0, firstDay);
            
            app.setDaySchedule(validDaySchedules);
            
            appointments = createMultipleDaysAppointments(app, appGroupId);
        }
        return appointments;
    }
        

    
    // private LocalTime addMinutesToTime(LocalTime time, int minutesToAdd) {
    //     int totalMinutes = time.getHour() * 60 + time.getMinute();
    //     totalMinutes = totalMinutes + minutesToAdd;
        
    //     int newHour = totalMinutes / 60;
    //     int newMinute = totalMinutes % 60;
        
    //     if (newHour >= 24) {
    //         newHour = newHour % 24;
    //     }
        
    //     return LocalTime.of(newHour, newMinute);
    // }
    
    private List<Appointment> createSingleDayAppointments(Appointment app, String groupId) {
        List<Appointment> appointments = new ArrayList<>();
        LocalTime currentStartTime = app.getStartTime();
        LocalTime endTime = app.getEndTime();
        int duration = app.getDurationMinutes();
        int gap = app.getGapMinutes() != null ? app.getGapMinutes() : 0;
        int slotNumber = 1;
        
        while (currentStartTime.plusMinutes(duration).isBefore(endTime) || currentStartTime.plusMinutes(duration).equals(endTime)) {
            LocalTime appointmentEndTime = currentStartTime.plusMinutes(duration);
            if (appointmentEndTime.isBefore(currentStartTime)) {
                break;
            }
            
            Appointment newApp = new Appointment();
            newApp.setTitle(app.getTitle() + " - Slot " + slotNumber);
            newApp.setDescription(app.getDescription());
            newApp.setLocation(app.getLocation());
            newApp.setAppointmentType(app.getAppointmentType());
            newApp.setDate(app.getDate());
            newApp.setStartTime(currentStartTime);
            newApp.setEndTime(appointmentEndTime);
            newApp.setDurationMinutes(duration);
            newApp.setGapMinutes(gap);
            newApp.setGroupId(groupId);
            newApp.setScheduleType("single");
            appointments.add(appointmentRepository.save(newApp));
            
            LocalTime nextStartTime = currentStartTime.plusMinutes(duration + gap);
            if (nextStartTime.isBefore(currentStartTime)) {
                break;
            }
            currentStartTime = nextStartTime;
            slotNumber++;
        }
        
        return appointments;
    }

    private List<Appointment> createMultipleDaysAppointments(Appointment app, String groupId) {
        List<Appointment> appointments = new ArrayList<>();
        int slotNumber = 1;
        
        for (var daySchedule : app.getDaySchedule()) {
            LocalTime currentStartTime = daySchedule.getStartTime();
            LocalTime endTime = daySchedule.getEndTime();
            int duration = app.getDurationMinutes();
            int gap = app.getGapMinutes() != null ? app.getGapMinutes() : 0;
            
            while (currentStartTime.plusMinutes(duration).isBefore(endTime) || currentStartTime.plusMinutes(duration).equals(endTime)) {
                LocalTime appointmentEndTime = currentStartTime.plusMinutes(duration);
                if (appointmentEndTime.isBefore(currentStartTime)) {
                    break;
                }
                
                Appointment newApp = new Appointment();
                newApp.setTitle(app.getTitle() + " - Slot " + slotNumber);
                newApp.setDescription(app.getDescription());
                newApp.setLocation(app.getLocation());
                newApp.setAppointmentType(app.getAppointmentType());
                newApp.setDate(daySchedule.getDate());
                newApp.setStartTime(currentStartTime);
                newApp.setEndTime(appointmentEndTime);
                newApp.setDurationMinutes(duration);
                newApp.setGapMinutes(gap);
                newApp.setGroupId(groupId);
                newApp.setScheduleType("multiple");
                appointments.add(appointmentRepository.save(newApp));
                
                LocalTime nextStartTime = currentStartTime.plusMinutes(duration + gap);
                if (nextStartTime.isBefore(currentStartTime)) {
                    break;
                }
                currentStartTime = nextStartTime;
                slotNumber++;
            }
        }
        
        return appointments;
    }

  @Override
    public Appointment bookAppointment(int appointmentId, User user) throws AppointmentAlreadyBookedException, AppointmentNotFoundException {
        
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent() == false) {
            throw new AppointmentNotFoundException("Appointment not found!");
        }
        Appointment appointment = appointmentOptional.get();
        if (appointment.isBooked() == true) {
            throw new AppointmentAlreadyBookedException("Appointment is already booked");
        }
        String appointmentType = appointment.getAppointmentType();
        String appointmentGroupId = appointment.getGroupId();

        if (appointmentType.equals("Group")) {
            String userGroup = user.getGroupId();
            if (userGroup == null) {
                throw new AppointmentAlreadyBookedException("This is a group appointment, but you are not assigned to any group");
            }
            if (userGroup.isEmpty()) {
                throw new AppointmentAlreadyBookedException("This is a group appointment, but you are not assigned to any group");
            }
            List<User> groupMembers = userRepository.findByGroupId(userGroup);
            for (int i = 0; i < groupMembers.size(); i++) {
                User member = groupMembers.get(i);
                List<Appointment> memberAppointments = appointmentRepository.findByBookedBy(member);
                for (int j = 0; j < memberAppointments.size(); j++) {
                    Appointment bookedAppointment = memberAppointments.get(j);
                    String bookedGroupId = bookedAppointment.getGroupId();
                    if (bookedGroupId.equals(appointmentGroupId)) {
                        String message = "A member of your group (" + member.getUsername() + ") has already booked a slot in this appointment group";
                        throw new AppointmentAlreadyBookedException(message);
                    }
                }
            }
        }
        

        if (appointmentType.equals("Individual")) {
            List<Appointment> userAppointments = appointmentRepository.findByBookedBy(user);
            for (int i = 0; i < userAppointments.size(); i++) {
                Appointment bookedAppointment = userAppointments.get(i);
                String bookedGroupId = bookedAppointment.getGroupId();
                if (bookedGroupId.equals(appointmentGroupId)) {
                    throw new AppointmentAlreadyBookedException("You have already booked an appointment in this appointment group");
                }
            }
        }
        
        appointment.setBooked(true);
        appointment.setBookedBy(user);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return savedAppointment;
    }
    @Override
    public boolean hasUserBookedAppointment(User user) {
        List<Appointment> userAppointments = appointmentRepository.findByBookedBy(user);
        if (userAppointments.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void cancelAppointment(int appointmentId, User user) throws AppointmentNotFoundException, CancelAppointmentException {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        
        if (!appointmentOptional.isPresent()) {
            throw new AppointmentNotFoundException("Appointment not found!!");
        }
        Appointment appointment = appointmentOptional.get();
        String userRole = user.getRole();
        if (userRole.equals("PROFESSOR")) {
            cancelAppointmentSlot(appointment);
        
        } 
        else if (userRole.equals("STUDENT")) {
            User bookedByUser = appointment.getBookedBy();
            if (bookedByUser == null || bookedByUser.getId() != user.getId()) {
                throw new CancelAppointmentException("You can only cancel appointments that you have booked!");
            }
            boolean canCancel = canStudentCancel(appointment);
            if (!canCancel) {
                throw new CancelAppointmentException("You can only cancel before the first appointment in the group!");
            }
            cancelAppointmentSlot(appointment);
            
        } else {
            throw new CancelAppointmentException("Invalid user role!");
        }
    }

    private boolean canStudentCancel(Appointment appointment) {
        String groupId = appointment.getGroupId();
        List<Appointment> groupAppointments = appointmentRepository.findByGroupIdOrderByDateAscStartTimeAsc(groupId);
        
        if (groupAppointments.isEmpty()) {
            return true;
        }
        Appointment firstAppointment = groupAppointments.get(0);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate firstDate = firstAppointment.getDate();
        LocalTime firstTime = firstAppointment.getStartTime();
        LocalDateTime firstAppointmentDateTime = LocalDateTime.of(firstDate, firstTime);
        return currentTime.isBefore(firstAppointmentDateTime);
    }

    private void cancelAppointmentSlot(Appointment appointment) {
        appointment.setBooked(false);
        appointment.setBookedBy(null);
        appointmentRepository.save(appointment);
    }

    
    @Override
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public boolean isGroupBooked(String groupId) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment currentAppointment = allAppointments.get(i);
            
            if (currentAppointment.getGroupId().equals(groupId)) {
                if (currentAppointment.isBooked() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateAppointmentGroup(String groupId, String title, String description, String location) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        int counter = 1;
        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment apt = allAppointments.get(i);
            if (apt.getGroupId().equals(groupId)) {
                String newTitle = title + " - Slot " + counter;
                apt.setTitle(newTitle);
                apt.setDescription(description);
                apt.setLocation(location);
                appointmentRepository.save(apt);
                counter++;
            }
        }
    }

    @Override
    public Appointment findFirstAppointmentByGroupId(String groupId) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        for (int index = 0; index < allAppointments.size(); index++) {
            Appointment currentApt = allAppointments.get(index);
            if (currentApt.getGroupId().equals(groupId)) {
                return currentApt;
            }
        }
        return null;
    }

    @Override
    public void deleteAppointmentGroup(String groupId) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appointment = allAppointments.get(i);
            if (appointment.getGroupId().equals(groupId)) {
                appointmentRepository.delete(appointment);
            }
        }
    }
}
