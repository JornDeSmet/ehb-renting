package com.example.ehbrenting.dto.user;

import com.example.ehbrenting.model.User;
import lombok.Data;

@Data
public class AdminUpdateUserDTO {

    private User.Role role;

    private boolean enabled;
}
