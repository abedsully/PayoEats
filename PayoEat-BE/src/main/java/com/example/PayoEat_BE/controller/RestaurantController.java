package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.dto.RestaurantStatusDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.image.IImageService;
import com.example.PayoEat_BE.service.notification.INotificationService;
import com.example.PayoEat_BE.service.orders.IOrderService;
import com.example.PayoEat_BE.service.user.IUserService;
import com.example.PayoEat_BE.service.restaurant.IRestaurantService;
import com.example.PayoEat_BE.dto.RestaurantDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
    private final INotificationService notificationService;
    private final IImageService imageService;

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

    @GetMapping("/detail-restaurant")
    @Operation(summary = "Getting details of restaurant for approval", description = "This endpoint is used for getting restaurant detail for approval")
    public ResponseEntity<ApiResponse> getRestaurantDetailForApproval(@RequestParam UUID id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantDetailForApproval(id);
            return ResponseEntity.ok(new ApiResponse("Restaurant found", restaurant));
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
                                                     @RequestParam("userRoles") UserRoles userRoles,
                                                     @RequestParam("restaurantName") String restaurantName,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("openingHour") String openingHour,
                                                     @RequestParam("closingHour") String closingHour,
                                                     @RequestParam("location") String location,
                                                     @RequestParam("telephoneNumber") String telephoneNumber,
                                                     @RequestParam("restaurantCategoryCode") Long restaurantCategoryCode,
                                                     @RequestParam("restaurantColor") String restaurantColor,
                                                     @RequestParam("restaurantImage") MultipartFile restaurantImage,
                                                     @RequestParam("qrisImage") MultipartFile qrisImage
                                                     ) {
        try {
            RegisterRestaurantRequest request = new RegisterRestaurantRequest(
                    email, password, userRoles, restaurantName, description, parseTime(openingHour), parseTime(closingHour), location, telephoneNumber, restaurantCategoryCode, restaurantColor
            );
            Restaurant newRestaurant = restaurantService.addRestaurant(request, restaurantImage, qrisImage);
            ReviewRestaurantRequest requestApproval = new ReviewRestaurantRequest(newRestaurant.getId(), newRestaurant.getName(), newRestaurant.getRestaurantImage(), newRestaurant.getUserId());
            RestaurantApproval newRestaurantApproval = restaurantService.addRestaurantApproval(requestApproval);
            RestaurantApprovalDto convertedRestaurantApproval = restaurantService.convertApprovalToDto(newRestaurantApproval);
            notificationService.addRestaurantApprovalNotification(newRestaurantApproval.getId(), newRestaurant.getId());
            notificationService.addUserNotification(newRestaurantApproval.getUserId(), newRestaurantApproval.getId());
            return ResponseEntity.ok(new ApiResponse("Your restaurant request has been added, Please wait for our admin to process your restaurant!", convertedRestaurantApproval));
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

    @GetMapping("/get-restaurant-by-name")
    @Operation(summary = "Get Restaurants By Name", description = "Getting list of restaurants based on their name")
    public ResponseEntity<ApiResponse> getRestaurantByName(@RequestParam String name) {
        try {
            List<Restaurant> restaurants = restaurantService.findRestaurantByName(name);

            if (restaurants.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No restaurants found with name: " + name, null));
            }

            List<RestaurantDto> convertedRestaurants = restaurantService.getConvertedRestaurants(restaurants);

            return ResponseEntity.ok(new ApiResponse("Found", convertedRestaurants));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update-restaurant/{id}")
    @Operation(summary = "Updating restaurant by id", description = "API for updating restaurant")
    public ResponseEntity<ApiResponse> updateRestaurant(@PathVariable UUID id, @RequestBody UpdateRestaurantRequest request) {
        try {
            Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, request);
            RestaurantDto convertedRestaurant = restaurantService.convertToDto(updatedRestaurant);
            return ResponseEntity.ok(new ApiResponse("Restaurant updated successfully", convertedRestaurant));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-restaurant/{id}")
    @Operation(summary = "Deleting restaurant by id", description = "API for deleting restaurant")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable UUID id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.ok(new ApiResponse("Restaurant deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/image")
    @Operation(summary = "Show Restaurant Image by ID", description = "API to display menu image by providing the image ID")
    public ResponseEntity<ByteArrayResource> showRestaurantImage(@RequestParam UUID imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, image.getFileType()).body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(null);
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

}