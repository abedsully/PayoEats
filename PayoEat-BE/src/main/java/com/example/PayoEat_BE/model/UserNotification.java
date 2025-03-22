package com.example.PayoEat_BE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_notifications")
@AllArgsConstructor
@NoArgsConstructor
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private UUID approvalId;
    private String message;
}
