package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ConfirmedOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private String orderStatus;
    private String customerName;
    private List<MenuListDto> menuLists;
    private String orderMessage;
    private String paymentImageUrl;
    private Double totalPrice;
    private Double subTotal;
    private LocalDateTime paymentBeginAt;
}
