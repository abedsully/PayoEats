package com.example.PayoEat_BE.request.order;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
public class ProcessPaymentRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Order ID is required")
    private UUID orderId;
}