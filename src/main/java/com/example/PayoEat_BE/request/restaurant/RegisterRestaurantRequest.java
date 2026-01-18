package com.example.PayoEat_BE.request.restaurant;

import com.example.PayoEat_BE.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RegisterRestaurantRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotNull(message = "Role ID is required")
    @Positive(message = "Role ID must be positive")
    private Long roleId;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(min = 1, max = 100, message = "Restaurant name must be between 1 and 100 characters")
    private String restaurantName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Opening hour is required")
    private LocalTime openingHour;

    @NotNull(message = "Closing hour is required")
    private LocalTime closingHour;

    @NotBlank(message = "Location cannot be blank")
    @Size(min = 1, max = 500, message = "Location must be between 1 and 500 characters")
    private String location;

    @NotBlank(message = "Telephone number cannot be blank")
    @Pattern(regexp = "^[0-9+\\s\\-()]{8,20}$", message = "Invalid telephone number format")
    private String telephoneNumber;

    @NotNull(message = "Restaurant category is required")
    @Positive(message = "Restaurant category ID must be positive")
    private Long restaurantCategory;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be in hex format (e.g., #FF0000)")
    private String color;
}