package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ProgressOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private String restaurantName;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String additionalInfo;
    private Integer paymentImageRejectionCount;
    private LocalDateTime orderTime;
    private LocalDateTime paymentUploadedAt;
    private LocalDateTime scheduledCheckInTime;
    private LocalDateTime paymentConfirmedAt;
}
