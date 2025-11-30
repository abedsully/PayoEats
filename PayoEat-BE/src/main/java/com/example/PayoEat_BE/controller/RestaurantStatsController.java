package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DashboardResponseDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.restaurant_stats.IRestaurantStatsService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurant-stats")
public class RestaurantStatsController {
    private final IRestaurantStatsService restaurantStatsService;
    private final IUserService userService;

    @GetMapping("/today")
    @Operation(summary = "Get Today's Restaurant Stats", description = "Getting today's restaurant status")
    public ResponseEntity<ApiResponse> getAllRestaurants(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            TodayRestaurantStatsDto result = restaurantStatsService.getTodayRestaurantStats(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Found", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get Complete Dashboard Data", description = "Get all dashboard statistics including today vs yesterday, weekly comparison, and popular items. Supports custom date ranges.")
    public ResponseEntity<ApiResponse> getDashboard(
            @RequestParam UUID restaurantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "7") Integer days
    ) {
        try {
            User user = userService.getAuthenticatedUser();
            DashboardResponseDto result = restaurantStatsService.getCompleteDashboard(
                    restaurantId, user.getId(), startDate, endDate, days
            );
            return ResponseEntity.ok(new ApiResponse("Success", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }
}
