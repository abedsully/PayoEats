package com.example.PayoEat_BE.request.restaurant;

import lombok.Data;

import java.time.LocalTime;

@Data
public class AddRestaurantRequest {
    private String name;
    private Double rating;
    private String description;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private String location;
    private String telephoneNumber;
}
