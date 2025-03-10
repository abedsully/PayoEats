package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.model.RestaurantCategory;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.restaurantCategory.IRestaurantCategoryService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant-category")
@Tag(name = "Restaurant Category Controller", description = "Endpoint for managing restaurant category")
public class RestaurantCategoryController {
    private final IRestaurantCategoryService restaurantCategoryService;
    private final IUserService userService;

    @PostMapping("/add")
    @Operation(summary = "Adding new restaurant category", description = "Returning a freshly built restaurant category")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> addCategory(@RequestParam String categoryName) {
        try {
            User user = userService.getAuthenticatedUser();
            RestaurantCategory restaurantCategory = restaurantCategoryService.addCategory(categoryName, user.getId());
            return ResponseEntity.ok(new ApiResponse("Restaurant category is successfully added", restaurantCategory));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-all")
    @Operation(summary = "Getting all available restaurant category", description = "Returning list of available restaurant category")
    public ResponseEntity<ApiResponse> getAll() {
        try {
            List<RestaurantCategory> restaurantCategoryList = restaurantCategoryService.getAllRestaurantCategory();
            if (restaurantCategoryList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("Currently, there are no available restaurant category", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found: ", restaurantCategoryList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
