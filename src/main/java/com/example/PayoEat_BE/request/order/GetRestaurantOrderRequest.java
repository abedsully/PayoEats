package com.example.PayoEat_BE.request.order;

import com.example.PayoEat_BE.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class GetRestaurantOrderRequest {
    private UUID restaurantId;
    private LocalDate localDate;
    private OrderStatus orderStatus;
}
