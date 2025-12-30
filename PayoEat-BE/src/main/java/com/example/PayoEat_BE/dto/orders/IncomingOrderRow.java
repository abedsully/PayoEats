package com.example.PayoEat_BE.dto.orders;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class IncomingOrderRow {
    private UUID orderId;
    private LocalDateTime orderTime;
    private String orderStatus;
    private String customerName;
    private UUID menuCode;
    private Long quantity;
    private String orderMessage;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
    private Long tax;
}

