package com.example.PayoEat_BE.service.restaurant_stats;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;

import java.util.UUID;

public interface IRestaurantStatsService {
    TodayRestaurantStatsDto getTodayRestaurantStats(UUID restaurantId, Long userId);
}
