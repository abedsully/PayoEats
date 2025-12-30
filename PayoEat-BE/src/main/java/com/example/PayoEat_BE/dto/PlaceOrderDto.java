package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PlaceOrderDto {
    private UUID orderId;
    private String customerId;
}
