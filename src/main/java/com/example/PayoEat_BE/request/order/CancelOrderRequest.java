package com.example.PayoEat_BE.request.order;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
public class CancelOrderRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotBlank(message = "Cancellation reason cannot be blank")
    @Size(min = 1, max = 500, message = "Cancellation reason must be between 1 and 500 characters")
    private String cancellationReason;
}