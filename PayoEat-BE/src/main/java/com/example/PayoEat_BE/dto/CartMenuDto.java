package com.example.PayoEat_BE.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartMenuDto {
    private String restaurantName;
    private List<MenuDto> menuDtos;
}
