package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class IncomingOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private List<MenuListDto> menuLists;
}

