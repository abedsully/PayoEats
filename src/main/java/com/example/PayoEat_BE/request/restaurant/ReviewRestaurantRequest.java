package com.example.PayoEat_BE.request.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReviewRestaurantRequest {

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(min = 1, max = 100, message = "Restaurant name must be between 1 and 100 characters")
    private String restaurantName;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
    private String restaurantImageUrl;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
}