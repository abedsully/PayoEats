package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
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
}
