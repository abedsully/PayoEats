package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RestaurantApprovalDto {
    private UUID restaurantId;
    private LocalDateTime requestedAt;
    private Boolean isApproved;
}
