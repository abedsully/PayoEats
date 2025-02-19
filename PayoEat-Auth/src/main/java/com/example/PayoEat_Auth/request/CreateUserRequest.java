package com.example.PayoEat_Auth.request;

import com.example.PayoEat_Auth.enums.UserRoles;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private UserRoles roles;
}
