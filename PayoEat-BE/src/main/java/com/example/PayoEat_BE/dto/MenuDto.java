package com.example.PayoEat_BE.dto;

import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Restaurant;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuDto {
    private String menuCode;
    private String menuName;
    private String menuDetail;
    private Double menuPrice;
    private UUID menuImage;
    private String menuImageUrl;
}
