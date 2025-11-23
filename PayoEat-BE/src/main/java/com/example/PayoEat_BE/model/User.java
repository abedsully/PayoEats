package com.example.PayoEat_BE.model;

import com.example.PayoEat_BE.enums.UserRoles;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

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
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String confirmationToken;
    private Boolean isActive;
}

