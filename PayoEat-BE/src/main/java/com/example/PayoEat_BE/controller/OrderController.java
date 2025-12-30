package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.dto.orders.OrderDetailResponseDto;
import com.example.PayoEat_BE.dto.orders.OrderHistoryDto;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.orders.IOrderService;
import com.example.PayoEat_BE.service.restaurant.RestaurantService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Component
@Tag(name = "Order Controller", description = "Endpoint for managing order")
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;
    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final List<UUID> trackedOrderIds = new CopyOnWriteArrayList<>();
    private final List<UUID> trackedRestaurantIds = new CopyOnWriteArrayList<>();
    private final List<UUID> trackedProgressOrderIds = new CopyOnWriteArrayList<>();

    private final RestaurantService restaurantService;

    @Value("${fe.url}")
    private String feUrl;


    @GetMapping("/details-order-by-customer")
    @Operation(summary = "Getting order details", description = "Returning details of an order")
    public ResponseEntity<ApiResponse> getOrderByIdCustomer(@RequestParam UUID orderId) {
        try {
            OrderDetailResponseDto order = orderService.getOrderByIdCustomer(orderId);
            return ResponseEntity.ok(new ApiResponse("Found: ", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/place")
    @Operation(summary = "Adding order to a restaurant", description = "Making order request to a restaurant")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest request) {

        try {
            PlaceOrderDto result = orderService.addOrder(request);
            return ResponseEntity.ok(new ApiResponse("Order has been received, Please wait for the restaurant to confirm your order", result));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/add-payment-proof", consumes = {"multipart/form-data"})
    @Operation(summary = "Adding payment proof to an order id", description = "Paying for order")
    public ResponseEntity<ApiResponse> sendPayment(@RequestParam UUID orderId, @RequestParam MultipartFile paymentProof) {
        try {
            orderService.addPaymentProof(orderId, paymentProof);
            return ResponseEntity.ok(new ApiResponse("Payment proof received, please wait", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-incoming-order")
    @Operation(summary = "Getting the list of incoming orders", description = "Returning list of incoming orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getIncomingOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<IncomingOrderDto> incomingOrderLists = orderService.getIncomingOrder(restaurantId);
            return ResponseEntity.ok(new ApiResponse("Successful", incomingOrderLists));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-confirmed-order")
    @Operation(summary = "Getting the list of incoming orders", description = "Returning list of incoming orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getConfirmedOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<ConfirmedOrderDto> confirmedOrder = orderService.getConfirmedOrder(restaurantId);
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", confirmedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-active")
    @Operation(summary = "Getting the list of active orders", description = "Returning list of active orders")
    public ResponseEntity<ApiResponse> getActiveOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<ActiveOrderDto> orderList = orderService.getActiveOrder(restaurantId);
            return ResponseEntity.ok(new ApiResponse("Getting list of active order is successful!", orderList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> confirmOrder(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            orderService.confirmOrder(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/confirm-payment")
    @Operation(summary = "Confirming an order payment of user", description = "Confirming order payment of user")
    public ResponseEntity<ApiResponse> confirmOrderPayment(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            orderService.confirmOrderPayment(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reject")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> rejectOrder(@RequestBody CancelOrderRequest request) {
        try {
            User user = userService.getAuthenticatedUser();
            String result = orderService.cancelOrderByRestaurant(request, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order has been cancelled", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reject-payment")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> rejectOrderPayment(@RequestBody RejectOrderPaymentDto request) {
        try {
            User user = userService.getAuthenticatedUser();
            orderService.rejectOrderPayment(request, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order payment is rejected", "Order payment is rejected"));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/finish")
    @Operation(summary = "Finishing an order", description = "Finishing user's order, done by restaurant")
    public ResponseEntity<ApiResponse> finishOrder(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            String result = orderService.finishOrder(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Operation successful", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/mark-ready")
    @Operation(summary = "Mark order as ready", description = "Mark an order as ready for pickup by restaurant")
    public ResponseEntity<ApiResponse> markOrderReady(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            String result = orderService.markOrderReady(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Operation successful", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/confirm-redirect")
    public ResponseEntity<String> confirmRedirect(@RequestParam UUID orderId) {
        String html = String.format("""
    <html>
      <head>
        <title>Confirming Order...</title>
      </head>
      <body onload='document.forms[0].submit()'>
        <p>Processing your order... please wait.</p>
        <form method='POST' action='/api/order/confirm2'>
          <input type='hidden' name='orderId' value='%s'/>
        </form>
      </body>
    </html>
    """, orderId.toString());


        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    // POST endpoint to confirm order
    @PostMapping("/confirm2")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<String> confirmOrder2(@RequestParam UUID orderId) {
        try {
            String result = orderService.processOrderToActive(orderId);

            // HTML with JS redirect
            String html = """
                <html>
                  <head>
                    <title>Order Confirmed</title>
                    <script>
                      // Redirect after 1 second
                      setTimeout(() => {
                        window.location.href = "%s";
                      }, 1000);
                    </script>
                  </head>
                  <body>
                    <h1>Order confirmed!</h1>
                    <p>Redirecting to dashboard...</p>
                  </body>
                </html>
                """.formatted(feUrl);

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);

        } catch (Exception e) {
            // Error page fallback
            String errorHtml = String.format("""
    <html>
      <body>
        <h1>Error processing order</h1>
        <p>%s</p>
      </body>
    </html>
    """, e.getMessage());

            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        }
    }

    @GetMapping("/qr")
    @Operation(summary = "Getting order QR", description = "Getting order QR code")
    public ResponseEntity<ApiResponse> getOrderQR(@RequestParam UUID orderId) {
        try {
            String result = orderService.generateOrderIdQrCode(orderId);
            return ResponseEntity.ok(new ApiResponse("Nice job, order id: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/check-payment")
    @Operation(summary = "Checking order payment", description = "Checking order payment")
    public ResponseEntity<ApiResponse> checkOrderPayment(@RequestParam UUID orderId) {
        try {
            Boolean result = orderService.checkPayment(orderId);
            return ResponseEntity.ok(new ApiResponse("Order payment result: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @MessageMapping("/restaurant-recent-orders/request")
    public void handleRecentOrdersRequest(@Payload String restaurantIdStr) {
        try {
            UUID restaurantId = UUID.fromString(restaurantIdStr);

            // Track restaurantId for periodic updates
            if (!trackedRestaurantIds.contains(restaurantId)) {
                trackedRestaurantIds.add(restaurantId);
            }

            // Push initial data immediately
            List<RecentOrderDto> recentOrders = orderService.getRecentOrderLists(restaurantId);
            messagingTemplate.convertAndSend(
                    "/topic/restaurant-recent-orders/" + restaurantIdStr,
                    recentOrders
            );

        } catch (Exception e) {
            System.err.println("Error in handleRecentOrdersRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Scheduled task to periodically push recent orders to all subscribed restaurants
     */
    @Scheduled(fixedRate = 5000)
    public void sendPeriodicRecentOrders() {
        for (UUID restaurantId : trackedRestaurantIds) {
            try {
                List<RecentOrderDto> recentOrders = orderService.getRecentOrderLists(restaurantId);
                messagingTemplate.convertAndSend(
                        "/topic/restaurant-recent-orders/" + restaurantId,
                        recentOrders
                );
            } catch (Exception e) {
                System.err.println("Failed to send recent orders for: " + restaurantId);
                e.printStackTrace();
            }
        }
    }

    @MessageMapping("/order-progress/request")
    public void handleOrderRequest(UUID orderId) {
        // Save the orderId if not already tracked
        if (!trackedOrderIds.contains(orderId)) {
            trackedOrderIds.add(orderId);
        }

        // Send the first response immediately
        ProgressOrderDto progress = orderRepository.getProgressOrder(orderId);
        messagingTemplate.convertAndSend("/topic/order-progress/" + orderId, progress);
    }

    @GetMapping("/incoming")
    @Operation(summary = "Checking order payment", description = "Checking order payment")
    public ResponseEntity<ApiResponse> incoming(@RequestParam UUID restaurantId) {
        try {
            List<IncomingOrderDto>  result = orderService.getIncomingOrder(restaurantId);
            return ResponseEntity.ok(new ApiResponse("Order payment result: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/payment-modal-data")
    @Operation(summary = "Getting payment modal data", description = "Getting payment modal data")
    public ResponseEntity<ApiResponse> getPaymentModalData(@RequestParam UUID orderId) {
        try {
            PaymentModalDto result = orderService.getPaymentModalData(orderId);
            return ResponseEntity.ok(new ApiResponse("Payment modal data: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Scheduled task to push updates every 5 seconds
     */
    @Scheduled(fixedRate = 5000)
    public void sendPeriodicUpdates() {
        for (UUID orderId : trackedOrderIds) {
            ProgressOrderDto progress = orderRepository.getProgressOrder(orderId);
            messagingTemplate.convertAndSend("/topic/order-progress/" + orderId, progress);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendRestaurantOrders() {
        for (UUID restaurantId : trackedRestaurantIds) {
            try {
                List<IncomingOrderDto> incoming = orderService.getIncomingOrder(restaurantId);
                List<ConfirmedOrderDto> confirmed = orderService.getConfirmedOrder(restaurantId);
                List<ActiveOrderDto> active = orderService.getActiveOrder(restaurantId);

                Map<String, Object> payload = new HashMap<>();
                payload.put("incoming", incoming);
                payload.put("confirmed", confirmed);
                payload.put("active", active);

                messagingTemplate.convertAndSend("/topic/restaurant-orders/" + restaurantId, payload);

            } catch (Exception e) {
                System.err.println("Failed to send restaurant order update for: " + restaurantId);
                e.printStackTrace();
            }
        }
    }

    @MessageMapping("/restaurant-orders/request")
    public void handleRestaurantOrderRequest(@Payload String restaurantIdStr) {
        try {
            UUID restaurantId = UUID.fromString(restaurantIdStr);

            if (!trackedRestaurantIds.contains(restaurantId)) {
                trackedRestaurantIds.add(restaurantId);
            }

            // Immediate push
            List<IncomingOrderDto> incoming = orderService.getIncomingOrder(restaurantId);
            List<ConfirmedOrderDto> confirmed = orderService.getConfirmedOrder(restaurantId);
            List<ActiveOrderDto> active = orderService.getActiveOrder(restaurantId);

            Map<String, Object> payload = new HashMap<>();
            payload.put("incoming", incoming);
            payload.put("confirmed", confirmed);
            payload.put("active", active);

            messagingTemplate.convertAndSend("/topic/restaurant-orders/" + restaurantIdStr, payload);

        } catch (Exception e) {
            System.err.println("Error in handleRestaurantOrderRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/history/customer")
    @Operation(summary = "Get customer order history", description = "Retrieve order history for authenticated customer")
    public ResponseEntity<ApiResponse> getCustomerOrderHistory(
            @RequestParam String customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status
    ) {
        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

            List<OrderHistoryDto> history = orderService.getCustomerOrderHistory(
                    customerId,
                start,
                end,
                status
            );

            return ResponseEntity.ok(new ApiResponse("Order history retrieved successfully", history));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/history/restaurant")
    @Operation(summary = "Get restaurant order history", description = "Retrieve order history for restaurant")
    public ResponseEntity<ApiResponse> getRestaurantOrderHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status
    ) {
        try {
            User user = userService.getAuthenticatedUser();

            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

            List<OrderHistoryDto> history = orderService.getRestaurantOrderHistory(
                start,
                end,
                status,
                    user.getId()
            );

            return ResponseEntity.ok(new ApiResponse("Order history retrieved successfully", history));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
