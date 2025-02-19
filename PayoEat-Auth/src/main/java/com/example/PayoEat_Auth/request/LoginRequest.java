package com.example.PayoEat_Auth.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
