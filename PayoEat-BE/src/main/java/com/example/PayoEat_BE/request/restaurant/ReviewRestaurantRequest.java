package com.example.PayoEat_BE.request.restaurant;

import com.example.PayoEat_BE.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReviewRestaurantRequest {
    private UUID restaurantId;
    private String restaurantName;
    private UUID restaurantImage;
    private Long userId;
}
