package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuDto {
    private String menuCode;
    private String menuName;
    private String menuDetail;
    private Double menuPrice;
    private String menuImageUrl;
    private UUID restaurantId;
    private String restaurantName;
}
