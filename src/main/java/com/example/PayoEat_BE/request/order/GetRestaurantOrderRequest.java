package com.example.PayoEat_BE.request.order;

import com.example.PayoEat_BE.enums.OrderStatus;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class GetRestaurantOrderRequest {

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate localDate;

    private OrderStatus orderStatus;
}