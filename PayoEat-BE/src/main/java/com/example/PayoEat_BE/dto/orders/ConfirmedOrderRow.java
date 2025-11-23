package com.example.PayoEat_BE.dto.orders;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ConfirmedOrderRow {
    private UUID orderId;
    private ZonedDateTime orderTime;
    private UUID menuCode;
    private Long quantity;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
    private String paymentImageUrl;
}
