package com.example.PayoEat_BE.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID orderId;
    private UUID approvalId;
    private Long userId;
    private UUID restaurantId;
    private String message;
    private LocalTime requestTime;
    private LocalDate requestDate;
    private Boolean isActive;
}
