package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.dto.dashboard.RatingBreakdownDto;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantReviewStatsDto {
    private String restaurantName;
    private Double rating;
    private Long totalRating;
    private String color;
    private String restaurantImageUrl;

    private List<RatingBreakdownDto> ratingBreakdown;
}
