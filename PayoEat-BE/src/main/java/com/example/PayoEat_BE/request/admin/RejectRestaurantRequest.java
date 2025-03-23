package com.example.PayoEat_BE.request.admin;

import lombok.Data;

import java.util.UUID;

@Data
public class RejectRestaurantRequest {
    private UUID approvalId;
    private String rejectionMessage;
}
