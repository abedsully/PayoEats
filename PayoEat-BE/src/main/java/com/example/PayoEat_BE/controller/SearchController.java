package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.SearchMenuResultDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.search.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search Controller", description = "Endpoint for managing restaurants")
public class SearchController {
    private final SearchService searchService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/restaurant")
    @Operation(summary = "Getting details of restaurant", description = "This endpoint is used for getting restaurant detail")
    public ResponseEntity<ApiResponse> searchRestaurants(@RequestParam String query) {
        List<Restaurant> result = searchService.search(query);
        return ResponseEntity.ok(new ApiResponse("Restaurant found", result));
    }

    @GetMapping("/menu")
    @Operation(summary = "Getting search results of menu", description = "This endpoint is used for getting search menu")
    public ResponseEntity<ApiResponse> searchMenus(@RequestParam UUID restaurantId, @RequestParam String query) {
        List<SearchMenuResultDto> result = searchService.searchMenu(restaurantId, query);
        return ResponseEntity.ok(new ApiResponse("Menu found", result));
    }
}
