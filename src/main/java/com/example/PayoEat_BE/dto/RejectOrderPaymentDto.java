package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RejectOrderPaymentDto {
    private UUID orderId;
    private String rejectionReason;
}
