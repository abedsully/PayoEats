package com.example.PayoEat_BE.request.restaurant;

import com.example.PayoEat_BE.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RegisterRestaurantRequest {
    private String email;
    private String password;
    private Long roleId;
    private String restaurantName;
    private String description;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private String location;
    private String telephoneNumber;
    private Long restaurantCategory;
    private String color;
    private Long tax;
}
