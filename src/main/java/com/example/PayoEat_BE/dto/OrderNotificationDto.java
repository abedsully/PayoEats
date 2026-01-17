package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class OrderNotificationDto {
    private UUID id;
    private UUID orderId;
    private UUID restaurantId;
    private LocalTime requestTime;
    private LocalDate requestDate;
}
