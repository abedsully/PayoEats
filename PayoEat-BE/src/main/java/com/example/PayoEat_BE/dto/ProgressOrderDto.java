package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class ProgressOrderDto {
    private UUID orderId;
    private String restaurantName;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String additionalInfo;
}
