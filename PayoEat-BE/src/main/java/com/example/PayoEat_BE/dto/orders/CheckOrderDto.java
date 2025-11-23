package com.example.PayoEat_BE.dto.orders;

import com.example.PayoEat_BE.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckOrderDto {
    private UUID id;
    private UUID restaurantId;
    private OrderStatus orderStatus;
    private String paymentImageUrl;
    private Long paymentImageRejectionCount;
}
