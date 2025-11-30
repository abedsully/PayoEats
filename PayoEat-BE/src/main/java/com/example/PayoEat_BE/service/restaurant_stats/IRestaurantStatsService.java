package com.example.PayoEat_BE.service.restaurant_stats;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DashboardResponseDto;

import java.time.LocalDate;
import java.util.UUID;

public interface IRestaurantStatsService {
    TodayRestaurantStatsDto getTodayRestaurantStats(UUID restaurantId, Long userId);
    DashboardResponseDto getCompleteDashboard(UUID restaurantId, Long userId, LocalDate startDate, LocalDate endDate, Integer days);
}
