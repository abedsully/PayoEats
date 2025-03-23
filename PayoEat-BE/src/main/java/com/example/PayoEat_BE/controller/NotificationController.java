package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.OrderNotificationDto;
import com.example.PayoEat_BE.model.Notification;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.model.UserNotification;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.notification.INotificationService;
import com.example.PayoEat_BE.service.restaurant.IRestaurantService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@Tag(name = "Notification Controller", description = "Endpoint for managing notification")
public class NotificationController {
    private final INotificationService notificationService;
    private final IUserService userService;
    private final IRestaurantService restaurantService;

    @GetMapping("/orders")
    @Operation(summary = "Getting order notifications for restaurant", description = "Returning list of notifications for restaurant")
    public ResponseEntity<ApiResponse> getOrderNotification(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

            List<Notification> notificationList = notificationService.getOrderNotification(restaurant.getId(), user.getId());

            if (notificationList.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Currently there are no notifications: ", null));
            }

            List<OrderNotificationDto> orderNotificationDtos = notificationService.getConvertedOrderNotification(notificationList);

            return ResponseEntity.ok(new ApiResponse("Notification lists: ", orderNotificationDtos));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Getting user notifications", description = "Returning list of notifications for user")
    public ResponseEntity<ApiResponse> getUserNotification() {
        try {
            User user = userService.getAuthenticatedUser();
            List<UserNotification> notificationList = notificationService.getUserNotification(user.getId());
            return ResponseEntity.ok(new ApiResponse("Notification lists: ", notificationList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
