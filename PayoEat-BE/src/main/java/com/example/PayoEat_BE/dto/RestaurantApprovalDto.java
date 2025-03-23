package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RestaurantApprovalDto {
    private UUID id;
    private UUID restaurantId;
    private String restaurantName;
    private UUID restaurantImage;
    private LocalDateTime requestedAt;
    private Boolean isApproved;
}
