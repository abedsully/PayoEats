package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantManagementData;
import com.example.PayoEat_BE.dto.RestaurantStatusDto;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.IOrderService;
import com.example.PayoEat_BE.service.IUserService;
import com.example.PayoEat_BE.service.IRestaurantService;
import com.example.PayoEat_BE.dto.RestaurantDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
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
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        RestaurantDto convertedRestaurant = restaurantService.convertToDto(restaurant);
        return ResponseEntity.ok(new ApiResponse("Restaurant found", convertedRestaurant));
    }

    @GetMapping("/similar-restaurants")
    @Operation(summary = "Getting details of restaurant for approval", description = "This endpoint is used for getting restaurant detail for approval")
    public ResponseEntity<ApiResponse> getSimilarRestaurants(@RequestParam UUID id) {
        List<Restaurant> restaurants = restaurantService.getSimilarRestaurant(id);
        if (restaurants.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Currently there are no restaurants yet", null));
        }
        List<RestaurantDto> convertedRestaurants = restaurantService.getConvertedRestaurants(restaurants);
        return ResponseEntity.ok(new ApiResponse("Found", convertedRestaurants));
    }

    public static LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString);
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
                                                     @RequestParam("restaurantImageUrl") MultipartFile restaurantImageUrl,
                                                     @RequestParam("qrisImageUrl") MultipartFile qrisImageUrl
                                                     ) {
        RegisterRestaurantRequest request = new RegisterRestaurantRequest(
                email, password, roleId, restaurantName, description, parseTime(openingHour), parseTime(closingHour), location, telephoneNumber, restaurantCategoryCode, restaurantColor
        );
        UUID restaurantId = restaurantService.addRestaurant(request, restaurantImageUrl, qrisImageUrl);
        return ResponseEntity.ok(new ApiResponse("Your restaurant has been registered! Please check your email to activate your account.", null));
    }


    @GetMapping("/all")
    @Operation(summary = "Get All Restaurants", description = "Getting all available restaurants")
    public ResponseEntity<ApiResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if (restaurants.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Currently there are no restaurants yet", null));
        }
        List<RestaurantDto> convertedRestaurants = restaurantService.getConvertedRestaurants(restaurants);
        return ResponseEntity.ok(new ApiResponse("Found", convertedRestaurants));
    }




    @GetMapping("/status")
    @Operation(summary = "Get restaurant status", description = "Getting restaurant status")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getRestaurantStatus(@RequestParam LocalDate date) {
        User user = userService.getAuthenticatedUser();
        RestaurantStatusDto result = orderService.restaurantOrderStatus(date, user.getId());
        return ResponseEntity.ok(new ApiResponse("Found", result));
    }

    @GetMapping("/get-id")
    @Operation(summary = "Get restaurant id by userId", description = "Getting restaurant id by userId")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getRestaurantStatus() {
        User user = userService.getAuthenticatedUser();
        UUID result = restaurantService.getRestaurantByUserId(user.getId());
        return ResponseEntity.ok(new ApiResponse("Found", result));
    }

    @GetMapping("/check-restaurant-name")
    @Operation(summary = "Check restaurant name duplicate", description = "Endpoint for checking if restaurant name already exists")
    public ResponseEntity<ApiResponse> checkRestaurantNameExists(@RequestParam String name) {
        Boolean exists = restaurantService.checkRestaurantNameExists(name);
        return ResponseEntity.ok(new ApiResponse("Check completed", exists));
    }

    @GetMapping("/get-management-data")
    @Operation(summary = "Get restaurant management data", description = "Getting restaurant management data")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getRestaurantManagementData(@RequestParam UUID restaurantId) {
        User user = userService.getAuthenticatedUser();
        RestaurantManagementData result = restaurantService.getRestaurantManagementData(restaurantId, user.getId());
        return ResponseEntity.ok(new ApiResponse("Found", result));
    }

    @PostMapping("/toggle-status")
    @Operation(summary = "Toggle restaurant active status", description = "Toggle restaurant open/closed status")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> toggleRestaurantStatus(
            @RequestParam UUID restaurantId,
            @RequestParam Boolean isActive) {
        User user = userService.getAuthenticatedUser();
        restaurantService.toggleRestaurantStatus(restaurantId, isActive, user.getId());
        return ResponseEntity.ok(new ApiResponse("Restaurant status updated successfully", null));
    }

    @PostMapping("/update-restaurant")
    @Operation(summary = "Update restaurant", description = "Update restaurant information")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> updateRestaurant(
            @RequestParam UUID restaurantId,
            @RequestParam String restaurantName,
            @RequestParam String description,
            @RequestParam String telephoneNumber,
            @RequestParam String location,
            @RequestParam String openingHour,
            @RequestParam String closingHour,
            @RequestParam String restaurantCategoryCode,
            @RequestParam(value = "restaurantImageUrl", required = false) MultipartFile restaurantImageUrl,
            @RequestParam(value = "qrisImageUrl", required = false) MultipartFile qrisImageUrl) {
        User user = userService.getAuthenticatedUser();
        UpdateRestaurantRequest updateRestaurantRequest = new UpdateRestaurantRequest(restaurantId, restaurantName, telephoneNumber, description, location, parseTime(openingHour), parseTime(closingHour), Long.parseLong(restaurantCategoryCode));

        restaurantService.updateRestaurant(updateRestaurantRequest, user.getId(), restaurantImageUrl, qrisImageUrl);
        return ResponseEntity.ok(new ApiResponse("Restaurant updated successfully", null));
    }

}
