package com.example.PayoEat_BE.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "Menu code is required")
    private UUID menuCode;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Min(value = 1, message = "Minimum quantity is 1")
    @Max(value = 100, message = "Maximum quantity is 100 per item")
    private Long quantity;
}