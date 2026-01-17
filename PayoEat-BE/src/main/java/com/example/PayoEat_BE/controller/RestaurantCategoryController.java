package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantCategoryDto;
import com.example.PayoEat_BE.model.RestaurantCategory;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.restaurant_category.IRestaurantCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant-category")
@Tag(name = "Restaurant Category Controller", description = "Endpoint for managing restaurant category")
public class RestaurantCategoryController {
    private final IRestaurantCategoryService restaurantCategoryService;

    @GetMapping("/all")
    @Operation(summary = "Getting all available restaurant category", description = "Returning list of available restaurant category")
    public ResponseEntity<ApiResponse> getAllRestaurantCategory() {
        List<RestaurantCategoryDto> restaurantCategoryList = restaurantCategoryService.getAllRestaurantCategory();

        if (restaurantCategoryList.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("No restaurant category available", null));
        }
        return ResponseEntity.ok(new ApiResponse("Found: ", restaurantCategoryList));
    }

    @GetMapping("/get-by-id")
    @Operation(summary = "Getting restaurant category by id", description = "Returning a single restaurant category")
    public ResponseEntity<ApiResponse> getRestaurantCategoryById(@RequestParam Long id) {
        RestaurantCategory result = restaurantCategoryService.getRestaurantCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Found: ", result));
    }
}
