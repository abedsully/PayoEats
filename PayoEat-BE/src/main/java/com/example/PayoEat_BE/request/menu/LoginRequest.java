package com.example.PayoEat_BE.request.menu;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

