package com.example.PayoEat_BE.request.review;

import lombok.Data;

import java.util.UUID;

@Data
public class AddReviewRequest {
    private String reviewContent;
    private Long userId;
    private UUID restaurantId;
}
