package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.restaurant_stats.IRestaurantStatsService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
