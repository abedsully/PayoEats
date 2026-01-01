package com.example.PayoEat_BE.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class IncomingOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private String orderStatus;
    private String customerName;
    private List<MenuListDto> menuLists;
    private Double totalPrice;
    private Double subTotal;
    private String orderMessage;
    private LocalDateTime orderTime;
}

