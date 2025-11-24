package com.example.PayoEat_BE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private Long roleId;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String confirmationToken;
    private Boolean isActive;
}

