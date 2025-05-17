package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.Image;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class RestaurantDto {
    private UUID id;
    private String name;
    private Double rating;
    private String description;
    private Long userId;
    private String telephoneNumber;
    private String location;
    private UUID restaurantImage;
    private String color;
    private Long restaurantCategory;
    private String categoryName;
    private LocalTime closingHour;
    private LocalTime openingHour;
}
