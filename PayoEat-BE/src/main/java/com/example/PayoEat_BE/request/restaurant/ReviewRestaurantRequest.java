package com.example.PayoEat_BE.request.restaurant;

import com.example.PayoEat_BE.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewRestaurantRequest {
    private Restaurant restaurant;
    private Long userId;
    private LocalDateTime requestedAt;
}
