package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchRestaurantResultDto {
    private String restaurantName;
    private UUID restaurantId;
}
