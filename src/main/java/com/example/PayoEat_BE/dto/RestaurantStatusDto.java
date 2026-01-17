package com.example.PayoEat_BE.dto;

import lombok.Data;

@Data
public class RestaurantStatusDto {
    private Long activeOrders;
    private Long completedOrders;
    private Long totalOrders;
    private Double totalIncome;
}
