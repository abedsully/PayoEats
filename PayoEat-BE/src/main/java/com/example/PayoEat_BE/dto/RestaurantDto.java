package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantDto {
    private Long id;
    private String name;
    private Double rating;
    private String description;
}
