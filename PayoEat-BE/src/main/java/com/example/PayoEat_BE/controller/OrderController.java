package com.example.PayoEat_BE.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test(@RequestParam UUID orderId) {
        try {
            String nice = orderService.generateOrderIdQrCode(orderId);
            return ResponseEntity.ok(new ApiResponse("Order lists: ", nice));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get")
    @Operation(summary = "Getting orders of a restaurant", description = "Returning list of orders of a restaurant")
    public ResponseEntity<ApiResponse> getOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<Order> restaurantOrderList = orderService.getOrderByRestaurantId(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order lists: ", restaurantOrderList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/details-order-by-restaurant")
    @Operation(summary = "Getting order details", description = "Returning details of an order")
    public ResponseEntity<ApiResponse> getOrderByIdRestaurant(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();

            Order order = orderService.getOrderByIdRestaurant(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Found: ", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/details-order-by-customer")
    @Operation(summary = "Getting order details", description = "Returning details of an order")
    public ResponseEntity<ApiResponse> getOrderByIdCustomer(@RequestParam UUID orderId) {
        try {
            Order order = orderService.getOrderByIdCustomer(orderId);
            return ResponseEntity.ok(new ApiResponse("Found: ", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/place")
    @Operation(summary = "Adding order to a restaurant", description = "Making order request to a restaurant")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest request) {

        try {
            Order newOrder = orderService.addOrder(request);

            notificationService.addOrderNotification(newOrder.getId(), newOrder.getRestaurantId());

            return ResponseEntity.ok(new ApiResponse("Order has been received, Please wait for the restaurant to confirm your order", newOrder.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/add-payment-proof", consumes = {"multipart/form-data"})
    @Operation(summary = "Adding payment proof to an order id", description = "Paying for order")
    public ResponseEntity<ApiResponse> sendPayment(@RequestParam UUID orderId, @RequestParam MultipartFile paymentProof) {
        try {
            Order order = orderService.addPaymentProof(orderId, paymentProof);
            return ResponseEntity.ok(new ApiResponse("Payment proof received, please wait", order.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }




    @GetMapping("/get-active")
    @Operation(summary = "Getting the list of active orders", description = "Returning list of active orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getActiveOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<Order> orderList = orderService.viewActiveOrders(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", orderList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> confirmOrder(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            Order order = orderService.confirmOrder(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/confirm-redirect")
    public ResponseEntity<String> confirmRedirect(@RequestParam UUID orderId) {
        String html = "<html><body onload='document.forms[0].submit()'>" +
                "<form method='POST' action='/api/order/confirm2'>" +
                "<input type='hidden' name='orderId' value='" + orderId + "'/>" +
                "</form></body></html>";
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    @PostMapping("/confirm2")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> confirmOrder2(@RequestParam UUID orderId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Nice job, order id: " + orderId, null));
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
            Order order = orderService.finishOrder(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order finished", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
