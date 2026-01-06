package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchRestaurantResultDto {
    private UUID restaurantId;
    private String restaurantName;
    private String restaurantImageUrl;
    private String description;
    private Double rating;
    private Long restaurantCategory;
}
