package com.example.PayoEat_BE.dto.orders;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderHistoryRow {
    private UUID orderId;
    private UUID restaurantId;
    private String restaurantName;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private Double subTotal;
    private Double totalPrice;
    private Double taxPrice;
    private UUID menuCode;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
    private Long quantity;
}
