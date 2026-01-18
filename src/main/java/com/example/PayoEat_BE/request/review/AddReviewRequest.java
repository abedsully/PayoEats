package com.example.PayoEat_BE.request.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {

    @NotBlank(message = "Review content cannot be blank")
    @Size(min = 1, max = 2000, message = "Review content must be between 1 and 2000 characters")
    private String reviewContent;

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double rating;

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;
}