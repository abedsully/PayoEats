package com.example.PayoEat_BE.request.order;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AddOrderRequest {
    private List<UUID> menuCode;
    private UUID restaurantId;
    private String orderMessage;
}
