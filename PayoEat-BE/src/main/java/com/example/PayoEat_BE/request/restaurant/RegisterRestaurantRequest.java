package com.example.PayoEat_BE.request.restaurant;

import com.example.PayoEat_BE.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RegisterRestaurantRequest {
    private String username;
    private String email;
    private String password;
    private UserRoles roles;
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
