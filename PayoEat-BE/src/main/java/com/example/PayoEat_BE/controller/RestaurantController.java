package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantApprovalDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.image.IImageService;
import com.example.PayoEat_BE.service.notification.INotificationService;
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
    private final IUserService userService;
    private final INotificationService notificationService;
    private final IImageService imageService;

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", description = "Returns a single restaurant based on its ID")
    public ResponseEntity<ApiResponse> getRestaurantById(@PathVariable UUID id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            RestaurantDto convertedRestaurant = restaurantService.convertToDto(restaurant);
            return ResponseEntity.ok(new ApiResponse("Restaurant found", convertedRestaurant));
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
    public ResponseEntity<ApiResponse> registerRestaurant(@RequestParam("username") String username,
                                                     @RequestParam("email") String email,
                                                     @RequestParam("password") String password,
                                                     @RequestParam("userRoles") UserRoles roles,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("rating") double rating,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("openingHour") String openingHour,
                                                     @RequestParam("closingHour") String closingHour,
                                                     @RequestParam("location") String location,
                                                     @RequestParam("telephoneNumber") String telephoneNumber,
                                                     @RequestParam("taxFee") double taxFee,
                                                     @RequestParam("restaurantCategory") Long restaurantCategory,
                                                     @RequestParam("restaurantImage") MultipartFile restaurantImage,
                                                     @RequestParam("qrisImage") MultipartFile qrisImage) {
        try {
            User user = userService.getAuthenticatedUser();
            RegisterRestaurantRequest request = new RegisterRestaurantRequest(
                    username, email, password, roles, name, rating, description, parseTime(openingHour), parseTime(closingHour), location, telephoneNumber, taxFee, restaurantCategory
            );
            Restaurant newRestaurant = restaurantService.addRestaurant(request, restaurantImage, qrisImage);
            ReviewRestaurantRequest requestApproval = new ReviewRestaurantRequest(newRestaurant.getId(), newRestaurant.getName(), newRestaurant.getRestaurantImage(), user.getId());
            RestaurantApproval newRestaurantApproval = restaurantService.addRestaurantApproval(requestApproval);
            RestaurantApprovalDto convertedRestaurantApproval = restaurantService.convertApprovalToDto(newRestaurantApproval);
            notificationService.addRestaurantApprovalNotification(newRestaurantApproval.getId(), newRestaurant.getId());
            notificationService.addUserNotification(user.getId(), newRestaurantApproval.getId());
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
    @Operation(summary = "Show Menu Image by ID", description = "API to display menu image by providing the image ID")
    public ResponseEntity<ByteArrayResource> showRestaurantImage(@RequestParam UUID imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, image.getFileType()).body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
    }

}