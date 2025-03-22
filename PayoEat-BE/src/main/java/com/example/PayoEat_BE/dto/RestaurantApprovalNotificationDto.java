package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RestaurantApprovalNotificationDto {
    private UUID id;
    private UUID restaruantId;
    private Long userId;
    private LocalDateTime requestedAt;

}
