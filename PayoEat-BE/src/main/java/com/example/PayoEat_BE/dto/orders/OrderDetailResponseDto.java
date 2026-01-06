package com.example.PayoEat_BE.dto.orders;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDetailResponseDto {
    private UUID orderId;
    private UUID restaurantId;
    private LocalDate createdDate;
    private LocalDateTime orderTime;
    private String orderMessage;
    private Double subTotal;
    private Double totalPrice;
    private String customerName;
    private LocalDateTime scheduledCheckInTime;

    private List<OrderItemDetailDto> items;
}
