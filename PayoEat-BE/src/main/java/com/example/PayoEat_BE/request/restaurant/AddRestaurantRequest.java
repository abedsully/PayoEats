package com.example.PayoEat_BE.request.restaurant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AddRestaurantRequest {
    @NotBlank(message = "Restaurant name cannot be blank")
    private String name;
    private Double rating;
    private String description;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private String location;
    private String telephoneNumber;
    private Double taxFee;
    private Long restaurantCategory;
}
