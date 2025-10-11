package com.example.appointment_scheduler.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//DTO for login
public class Login {

    @NotBlank(message = "Email is a required field!!")
    @Email(message = "Email should be valid!!", regexp = "^[A-Za-z0-9._%+-]+@iu\\.edu$")
    private String email;

    @NotBlank(message = "Password is a required field!!")
    private String password;

}
