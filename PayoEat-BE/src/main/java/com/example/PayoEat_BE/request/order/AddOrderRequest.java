package com.example.PayoEat_BE.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderRequest {
    private List<UUID> menuCode;
    private UUID restaurantId;
    private String orderMessage;
}
