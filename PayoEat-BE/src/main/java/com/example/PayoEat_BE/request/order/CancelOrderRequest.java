package com.example.PayoEat_BE.request.order;

import lombok.Data;

import java.util.UUID;

@Data
public class CancelOrderRequest {
    private UUID orderId;
    private String cancellationReason;
}
