package com.example.appointment_scheduler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Data
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor

public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Username is a required field!!")
    private String username;

    @NotBlank(message = "Password is a required field!!")
    private String password;

    @NotBlank(message = "Email is a required field!!")
    @Email(message = "Email should be valid!!", regexp = "^[A-Za-z0-9._%+-]+@iu\\.edu$")
    private String email;

    @NotBlank(message = "Role is a required field!!")
    private String role;

}
