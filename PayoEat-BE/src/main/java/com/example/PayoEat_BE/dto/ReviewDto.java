package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewDto {
    private String reviewContent;
    private LocalDateTime createdAt;
    private UUID restaurantId;
    private Long userId;
}
