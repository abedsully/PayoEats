package com.example.PayoEat_BE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private UUID id;
    private String name;
    private Double rating;
    private Long totalRatingCount;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Long userId;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private String location;
    private String telephoneNumber;
    private String restaurantImageUrl;
    private String qrisImageUrl;
    private String color;
    private Long restaurantCategory;
}
