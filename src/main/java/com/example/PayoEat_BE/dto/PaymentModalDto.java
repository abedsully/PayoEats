package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentModalDto {
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String name;
    private String qrisImageUrl;
    private Double totalPrice;
    private LocalDateTime paymentBeginAt;
    private String paymentImageRejectionReason;
}
