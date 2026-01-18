package com.example.PayoEat_BE.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderRequest {

    @NotNull(message = "Items cannot be null")
    @NotEmpty(message = "At least one item is required")
    @Size(max = 50, message = "Maximum 50 items allowed per order")
    private List<OrderItemRequest> items;

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @Size(max = 500, message = "Order message cannot exceed 500 characters")
    private String orderMessage;

    @NotNull(message = "Dine in time is required")
    private LocalTime dineInTime;

    @NotNull(message = "Quotas is required")
    @Positive(message = "Quotas must be positive")
    @Min(value = 1, message = "Minimum 1 quota required")
    private Long quotas;

    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 1, max = 100, message = "Customer name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Customer name can only contain letters and spaces")
    private String customerName;

    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;

    @NotNull(message = "Scheduled check-in time is required")
    @FutureOrPresent(message = "Scheduled check-in time must be in the present or future")
    private LocalDateTime scheduledCheckInTime;
}