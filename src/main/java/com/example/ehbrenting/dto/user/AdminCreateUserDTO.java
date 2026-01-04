package com.example.ehbrenting.dto.user;

import com.example.ehbrenting.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateUserDTO {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String firstName;
    private String lastName;

    private User.Role role = User.Role.STUDENT;

    private boolean enabled = true;
}
