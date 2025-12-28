package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantManagementData;
import com.example.PayoEat_BE.dto.RestaurantStatusDto;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.orders.IOrderService;
import com.example.PayoEat_BE.service.user.IUserService;
import com.example.PayoEat_BE.service.restaurant.IRestaurantService;
import com.example.PayoEat_BE.dto.RestaurantDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurant")
@Tag(name = "Restaurant Controller", description = "Endpoint for managing restaurants")
public class RestaurantController {
    private final IRestaurantService restaurantService;
    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping("/detail")
    @Operation(summary = "Getting details of restaurant", description = "This endpoint is used for getting restaurant detail")
    public ResponseEntity<ApiResponse> getRestaurantById(@RequestParam UUID id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            RestaurantDto convertedRestaurant = restaurantService.convertToDto(restaurant);
            return ResponseEntity.ok(new ApiResponse("Restaurant found", convertedRestaurant));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/similar-restaurants")
    @Operation(summary = "Getting details of restaurant for approval", description = "This endpoint is used for getting restaurant detail for approval")
    public ResponseEntity<ApiResponse> getSimilarRestaurants(@RequestParam UUID id) {
        try {
            List<Restaurant> restaurants = restaurantService.getSimilarRestaurant(id);
            if (restaurants.isEmpty()) {
                return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Currently there are no restaurants yet", null));
            }
            List<RestaurantDto> convertedRestaurants = restaurantService.getConvertedRestaurants(restaurants);
            return ResponseEntity.ok(new ApiResponse("Found", convertedRestaurants));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    public static LocalTime parseTime(String timeString) {
        try {
            return LocalTime.parse(timeString);
        } catch (DateTimeParseException e) {
            throw (e);
        }
    }

    @PostMapping(value = "/register-restaurant", consumes = {"multipart/form-data"})
    @Operation(summary = "Register Restaurant", description = "Register restaurant by request")
    public ResponseEntity<ApiResponse> registerRestaurant(
                                                     @RequestParam("email") String email,
                                                     @RequestParam("password") String password,
                                                     @RequestParam("userRoles") Long roleId,
                                                     @RequestParam("restaurantName") String restaurantName,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("openingHour") String openingHour,
                                                     @RequestParam("closingHour") String closingHour,
                                                     @RequestParam("location") String location,
                                                     @RequestParam("telephoneNumber") String telephoneNumber,
                                                     @RequestParam("restaurantCategoryCode") Long restaurantCategoryCode,
                                                     @RequestParam("restaurantColor") String restaurantColor,
                                                     @RequestParam("tax") String tax,
                                                     @RequestParam("restaurantImageUrl") MultipartFile restaurantImageUrl,
                                                     @RequestParam("qrisImageUrl") MultipartFile qrisImageUrl
                                                     ) {
        try {
            RegisterRestaurantRequest request = new RegisterRestaurantRequest(
                    email, password, roleId, restaurantName, description, parseTime(openingHour), parseTime(closingHour), location, telephoneNumber, restaurantCategoryCode, restaurantColor, Long.parseLong(tax)
            );
            UUID restaurantId = restaurantService.addRestaurant(request, restaurantImageUrl, qrisImageUrl);
            return ResponseEntity.ok(new ApiResponse("Your restaurant request has been added, Please wait for our admin to process your restaurant!", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }


    @GetMapping("/all")
    @Operation(summary = "Get All Restaurants", description = "Getting all available restaurants")
    public ResponseEntity<ApiResponse> getAllRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();
            if (restaurants.isEmpty()) {
                return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Currently there are no restaurants yet", null));
            }
            List<RestaurantDto> convertedRestaurants = restaurantService.getConvertedRestaurants(restaurants);
            return ResponseEntity.ok(new ApiResponse("Found", convertedRestaurants));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }




    @GetMapping("/status")
    @Operation(summary = "Get restaurant status", description = "Getting restaurant status")
    public ResponseEntity<ApiResponse> getRestaurantStatus(@RequestParam LocalDate date) {
        try {
            User user = userService.getAuthenticatedUser();
            RestaurantStatusDto result = orderService.restaurantOrderStatus(date, user.getId());

            return ResponseEntity.ok(new ApiResponse("Found", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/get-id")
    @Operation(summary = "Get restaurant id by userId", description = "Getting restaurant id by userId")
    public ResponseEntity<ApiResponse> getRestaurantStatus() {
        try {
            User user = userService.getAuthenticatedUser();
            UUID result = restaurantService.getRestaurantByUserId(user.getId());
            return ResponseEntity.ok(new ApiResponse("Found", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/get-management-data")
    @Operation(summary = "Get restaurant management data", description = "Getting restaurant management data")
    public ResponseEntity<ApiResponse> getRestaurantManagementData(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            RestaurantManagementData result = restaurantService.getRestaurantManagementData(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Found", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/toggle-status")
    @Operation(summary = "Toggle restaurant active status", description = "Toggle restaurant open/closed status")
    public ResponseEntity<ApiResponse> toggleRestaurantStatus(
            @RequestParam UUID restaurantId,
            @RequestParam Boolean isActive) {
        try {
            User user = userService.getAuthenticatedUser();
            restaurantService.toggleRestaurantStatus(restaurantId, isActive, user.getId());
            return ResponseEntity.ok(new ApiResponse("Restaurant status updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

}