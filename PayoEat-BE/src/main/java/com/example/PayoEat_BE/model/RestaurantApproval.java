package com.example.PayoEat_BE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantApproval {
    private UUID id;
    private UUID restaurantId;
    private String restaurantName;
    private String restaurantImageUrl;
    private Long userId;
    private String reason;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private Boolean isApproved;
    private Boolean isActive;
}
