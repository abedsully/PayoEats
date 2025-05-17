package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.dto.SearchResultDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.search.ISearchService;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        try {
            List<SearchResultDto> result = searchService.search(query);
            return ResponseEntity.ok(new ApiResponse("Restaurant found", result));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/searchRestaurantById")
    public ResponseEntity<ApiResponse> searchRestaurantById(@RequestParam UUID restaurantId) {
        try {
            Restaurant selectedRestaurant = restaurantRepository.findById(restaurantId).orElse(null);
            if (selectedRestaurant == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Restaurant not found", null));
            }

            List<SearchResultDto> relatedRestaurants = searchService.findRelatedRestaurants(selectedRestaurant);
            return ResponseEntity.ok(new ApiResponse("Search results", relatedRestaurants));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error during search", null));
        }
    }
}
