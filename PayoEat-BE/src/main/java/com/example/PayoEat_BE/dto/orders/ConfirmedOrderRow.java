package com.example.PayoEat_BE.dto.orders;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConfirmedOrderRow {
    private UUID orderId;
    private LocalDateTime orderTime;
    private String orderStatus;
    private String customerName;
    private String orderMessage;
    private UUID menuCode;
    private Long quantity;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
    private String paymentImageUrl;
    private LocalDateTime paymentBeginAt;
}
