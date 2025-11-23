package com.example.PayoEat_BE.dto.orders;

import lombok.Data;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ActiveOrderRow {
    private UUID orderId;
    private ZonedDateTime orderTime;
    private UUID menuCode;
    private Long quantity;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
    private LocalTime dineInTime;
}
