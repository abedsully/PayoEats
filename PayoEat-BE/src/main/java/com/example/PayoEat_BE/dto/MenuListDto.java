package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuListDto {
    private UUID menuCode;
    private String menuName;
    private Long quantity;
    private Double menuPrice;
    private Double totalPrice;
    private String menuImage;
}
