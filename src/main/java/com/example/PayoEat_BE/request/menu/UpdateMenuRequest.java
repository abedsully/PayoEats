package com.example.PayoEat_BE.request.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
public class UpdateMenuRequest {

    @NotBlank(message = "Menu name cannot be blank")
    @Size(min = 1, max = 100, message = "Menu name must be between 1 and 100 characters")
    private String menuName;

    @NotBlank(message = "Menu detail cannot be blank")
    @Size(min = 1, max = 500, message = "Menu detail must be between 1 and 500 characters")
    private String menuDetail;

    @NotNull(message = "Menu price is required")
    @Positive(message = "Menu price must be positive")
    @DecimalMin(value = "0.01", message = "Menu price must be at least 0.01")
    @DecimalMax(value = "999999.99", message = "Menu price cannot exceed 999999.99")
    private Double menuPrice;
}