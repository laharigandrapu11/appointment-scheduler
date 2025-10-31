package com.example.appointment_scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.error.AppointmentAlreadyBookedException;
import com.example.appointment_scheduler.error.CancelAppointmentException;
import com.example.appointment_scheduler.error.AppointmentNotFoundException;
import com.example.appointment_scheduler.model.Appointment;
import com.example.appointment_scheduler.service.AppointmentService;
import java.util.List;

@Controller
public class WebController {

    private final AppointmentService appointmentService;
    
    public WebController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @ModelAttribute(name = "appointment")
    public Appointment appointment() {
        return new Appointment();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "home";
    }
    
    @GetMapping("/appointments/book")
    public String viewAppointments(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Appointment> appointments = appointmentService.findAll();
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("user", user);
        return "view-appointments-student";
    }
    
    @GetMapping("/appointments/schedule")
    public String createAppointments(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        if (!"PROFESSOR".equals(user.getRole())) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        return "create-appointments";
    }
    
    @PostMapping("/appointments")
    public String processAppointments(@Valid Appointment request, Errors errors, HttpSession session, Model model) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        if (!"PROFESSOR".equals(user.getRole())) {
            return "redirect:/";
        }
        
        if (errors.hasErrors()) {
            model.addAttribute("user", user);
            return "create-appointments";
        }
        
        List<Appointment> createdAppointments = appointmentService.createAppointments(request);
        model.addAttribute("createdCount", createdAppointments.size());
        model.addAttribute("appointments", createdAppointments);
        return "appointments-success";
    }
    
    @GetMapping("/appointments/upcoming")
    public String viewUpcomingAppointments(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Appointment> appointments = appointmentService.findAll();
        model.addAttribute("appointments", appointments);
        model.addAttribute("user", user);
        return "view-appointments-prof";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/appointments/book/{id}")
    public String bookAppointment(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            appointmentService.bookAppointment(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");
            
        } catch (AppointmentNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Appointment not found!");
            
        } catch (AppointmentAlreadyBookedException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/appointments/book";
    }

    @PostMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            appointmentService.cancelAppointment(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled successfully!");
            
        } catch (AppointmentNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Appointment not found!");
            
        } catch (CancelAppointmentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        String userRole = user.getRole();
        
        if (userRole.equals("PROFESSOR")) {
            return "redirect:/appointments/upcoming";
        } else {
            return "redirect:/appointments/book";
        }
    }

    @PostMapping("/appointments/toggle/{groupId}")
    public String toggleAppointmentGroup(@PathVariable String groupId, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        String userRole = currentUser.getRole();
        if (userRole.equals("PROFESSOR") == false) {
            return "redirect:/";
        }
        List<Appointment> allAppointments = appointmentService.findAll();
        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appointment = allAppointments.get(i);
            String appointmentGroupId = appointment.getGroupId();
            if (appointmentGroupId.equals(groupId) == true) {
                boolean currentStatus = appointment.isActive();
                boolean newStatus = !currentStatus;
                appointment.setActive(newStatus);
                appointmentService.saveAppointment(appointment);
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Appointment group status updated!");
        return "redirect:/appointments/upcoming";
    }
    
    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}