package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;

@Data
public class WeeklyStatsDto {
    private Long weekOrders;
    private Double weekRevenue;
    private Long lastWeekOrders;
    private Double lastWeekRevenue;
}
