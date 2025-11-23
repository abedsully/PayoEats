package com.example.PayoEat_BE.dto.restaurants;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckUserRestaurantDto {
    private UUID id;
    private Long userId;
    private Long roleId;
}
