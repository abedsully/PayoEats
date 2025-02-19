package com.example.PayoEat_Auth.model;

import com.example.PayoEat_Auth.enums.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Table(name = "users")
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private UserRoles roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}

