package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class IncomingOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private List<MenuListDto> menuLists;
    private Double totalPrice;
    private Double subTotal;
    private Double taxPrice;
    private ZonedDateTime receivedAt;
}

