package com.example.PayoEat_BE.request.review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddReviewRequest {
    private String reviewContent;
    private UUID restaurantId;
    private Double rating;
    private String reviewImageUrl;
}
