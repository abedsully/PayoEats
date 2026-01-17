package com.example.PayoEat_BE.dto;

import lombok.Data;

@Data
public class TodayRestaurantStatsDto {
    private Long activeOrders;
    private Long completedOrders;
    private Double income;
}
