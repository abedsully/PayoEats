package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;

@Data
public class DashboardStatsDto {
    // Today vs Yesterday
    private Double todayRevenue;
    private Double yesterdayRevenue;
    private Long todayOrders;
    private Long yesterdayOrders;
    private Long todayCompleted;
    private Long yesterdayCompleted;
    private Long todayCancelled;
    private Long yesterdayCancelled;

    // Calculated metrics
    private Double avgOrderValue;
    private Double completionRate;
    private Double revenueChangePercent;
    private Double ordersChangePercent;

    // Weekly data
    private Double weekRevenue;
    private Double lastWeekRevenue;
    private Long weekOrders;
    private Long lastWeekOrders;
    private Double weekRevenueChangePercent;
    private Double weekOrdersChangePercent;
}
