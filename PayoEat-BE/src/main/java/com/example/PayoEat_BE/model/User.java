package com.example.PayoEat_BE.model;

import com.example.PayoEat_BE.enums.UserRoles;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRoles roles;

    @Column(name = "password", length = 256)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}

