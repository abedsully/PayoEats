package com.example.PayoEat_BE.request.menu;

import com.example.PayoEat_BE.enums.UserRoles;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
}
