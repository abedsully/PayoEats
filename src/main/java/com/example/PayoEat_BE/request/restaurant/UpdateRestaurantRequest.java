package com.example.PayoEat_BE.request.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateRestaurantRequest {

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(min = 1, max = 100, message = "Restaurant name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Telephone number cannot be blank")
    @Pattern(regexp = "^[0-9+\\s\\-()]{8,20}$", message = "Invalid telephone number format")
    private String telephoneNumber;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Location cannot be blank")
    @Size(min = 1, max = 500, message = "Location must be between 1 and 500 characters")
    private String location;

    @NotNull(message = "Opening hour is required")
    private LocalTime openingHour;

    @NotNull(message = "Closing hour is required")
    private LocalTime closingHour;

    @NotNull(message = "Restaurant category is required")
    @Positive(message = "Restaurant category ID must be positive")
    private Long restaurantCategory;
}