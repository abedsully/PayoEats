package com.example.PayoEat_BE.request.review;

import lombok.Data;

@Data
public class AddReviewRequest {
    private String reviewContent;
    private Long userId;
    private Long restaurantId;
}
