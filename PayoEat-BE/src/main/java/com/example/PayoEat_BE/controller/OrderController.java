package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.model.Notification;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.notification.INotificationService;
import com.example.PayoEat_BE.service.orders.IOrderService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name = "Order Controller", description = "Endpoint for managing order")
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;
    private final INotificationService notificationService;

    @GetMapping("/get")
    @Operation(summary = "Getting orders of a restaurant", description = "Returning list of orders of a restaurant")
    public ResponseEntity<ApiResponse> getOrders(UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<Order> restaurantOrderList = orderService.getOrderByRestaurantId(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order lists: ", restaurantOrderList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Adding order to a restaurant", description = "Making order request to a restaurant")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest request) {
        try {
            User user = userService.getAuthenticatedUser();
            Order newOrder = orderService.addOrder(request, user.getId());
            notificationService.addOrderNotification(newOrder.getId(), newOrder.getRestaurantId(), user.getId());
            return ResponseEntity.ok(new ApiResponse("Order received: ", newOrder));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/finish-order/{orderId}")
    @Operation(summary = "Finishing an order", description = "Finishing order request by restaurant")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> finishOrder(@PathVariable UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            orderService.finishOrder(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order finished", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
