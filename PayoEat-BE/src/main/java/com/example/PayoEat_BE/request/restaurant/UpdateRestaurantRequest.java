package com.example.PayoEat_BE.request.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateRestaurantRequest {
    private UUID restaurantId;
    private String name;
    private String telephoneNumber;
    private String description;
    private String location;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private Long restaurantCategory;
    private Long tax;
}
