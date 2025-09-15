package com.example.PayoEat_BE.dto;

import java.util.List;
import java.util.UUID;

public class ConfirmedOrderDto {
    private UUID restaurantId;
    private UUID orderId;
    private List<MenuListDto> menuLists;
    private UUID paymentImage;
}
