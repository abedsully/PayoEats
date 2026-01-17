package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchMenuResultDto {
    private UUID menuCode;
    private String menuName;
    private UUID menuImage;
    private String menuImageUrl;
}
