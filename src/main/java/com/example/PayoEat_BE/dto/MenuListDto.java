package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuListDto {
    private UUID menuCode;
    private String menuName;
    private Double menuPrice;
    private Long quantity;
    private Double totalPrice;
    private String menuImageUrl;
}
