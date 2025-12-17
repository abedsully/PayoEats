package com.example.PayoEat_BE.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderRequest {
    private List<OrderItemRequest> items;
    private UUID restaurantId;
    private String orderMessage;
    private LocalTime dineInTime;
    private Long quotas;
    private String customerName;
    private String customerId;
}
