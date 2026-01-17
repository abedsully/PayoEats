package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TopMenusDto {
    private UUID menuCode;
    private Long orderCount;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
}
