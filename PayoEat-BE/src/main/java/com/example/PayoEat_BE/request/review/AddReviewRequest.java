package com.example.PayoEat_BE.request.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    private String reviewContent;
    private UUID restaurantId;
    private Double rating;
    private UUID orderId;
    private String customerId;
}
