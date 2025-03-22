package com.example.PayoEat_BE.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID restaurantId;
    private String restaurantName;
    private UUID restaurantImage;
    private Long userId;
    private String reason;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private Boolean isApproved;
    private Boolean isActive;
}
