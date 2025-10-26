package com.example.appointment_scheduler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.appointment_scheduler.model.User;
import com.example.appointment_scheduler.repository.AppointmentRepository;
import com.example.appointment_scheduler.repository.UserRepository;

@SpringBootApplication
public class AppointmentSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentSchedulerApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository, AppointmentRepository appointmentRepository) {
		return args -> {
			User prof1 = new User();
			prof1.setUsername("Prof 1");
			prof1.setEmail("prof1@gmail.com");
			prof1.setPassword("password");
			prof1.setRole("PROFESSOR");
			userRepository.save(prof1);
			
			User prof2 = new User();
			prof2.setUsername("Prof 2");
			prof2.setEmail("prof2@gmail.com");
			prof2.setPassword("password");
			prof2.setRole("PROFESSOR");
			userRepository.save(prof2);
			
			User student1 = new User();
			student1.setUsername("Student 1");
			student1.setEmail("student1@gmail.com");
			student1.setPassword("password");
			student1.setRole("STUDENT");
			userRepository.save(student1);
			
			User student2 = new User();
			student2.setUsername("Student 2");
			student2.setEmail("student2@gmail.com");
			student2.setPassword("password");
			student2.setRole("STUDENT");
			userRepository.save(student2);
			
			
			System.out.println("loaded dummy data successfully!!!");
		};
	}

}
