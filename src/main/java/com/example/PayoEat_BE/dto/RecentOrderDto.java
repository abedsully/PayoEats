package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RecentOrderDto {
    private UUID orderId;
    private String customerName;
    private Long itemCount;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private LocalDate createdDate;
    private LocalDateTime orderTime;
}
