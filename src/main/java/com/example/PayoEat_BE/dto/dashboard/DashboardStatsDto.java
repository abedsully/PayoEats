package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;

@Data
public class DashboardStatsDto {
    private Double todayRevenue;
    private Double yesterdayRevenue;
    private Long todayOrders;
    private Long yesterdayOrders;
    private Long todayCompleted;
    private Long yesterdayCompleted;
    private Long todayCancelled;
    private Long yesterdayCancelled;

    private Double avgOrderValue;
    private Double completionRate;
    private Double revenueChangePercent;
    private Double ordersChangePercent;

    private Double weekRevenue;
    private Double lastWeekRevenue;
    private Long weekOrders;
    private Long lastWeekOrders;
    private Double weekRevenueChangePercent;
    private Double weekOrdersChangePercent;
}
