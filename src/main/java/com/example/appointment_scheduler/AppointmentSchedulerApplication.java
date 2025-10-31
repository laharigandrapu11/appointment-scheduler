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
			student1.setGroupId("GROUP_A");
			userRepository.save(student1);
			
			User student2 = new User();
			student2.setUsername("Student 2");
			student2.setEmail("student2@gmail.com");
			student2.setPassword("password");
			student2.setRole("STUDENT");
			student2.setGroupId("GROUP_A");
			userRepository.save(student2);
			
			User student3 = new User();
			student3.setUsername("Student 3");
			student3.setEmail("student3@gmail.com");
			student3.setPassword("password");
			student3.setRole("STUDENT");
			student3.setGroupId("GROUP_A");
			userRepository.save(student3);

			User student4 = new User();
			student4.setUsername("Student 4");
			student4.setEmail("student4@gmail.com");
			student4.setPassword("password");
			student4.setRole("STUDENT");
			student4.setGroupId(null);
			userRepository.save(student4);
			
			User ta1 = new User();
			ta1.setUsername("TA 1");
			ta1.setEmail("ta@gmail.com");
			ta1.setPassword("password");
			ta1.setRole("TA");
			userRepository.save(ta1);
			
			
			System.out.println("loaded dummy data successfully!!!");
		};
	}

}
