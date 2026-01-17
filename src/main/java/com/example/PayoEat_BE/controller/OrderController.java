package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.dto.orders.OrderDetailResponseDto;
import com.example.PayoEat_BE.dto.orders.OrderHistoryDto;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.IOrderService;
import com.example.PayoEat_BE.service.RestaurantService;
import com.example.PayoEat_BE.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
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

    @Value("${backend.url}")
    private String backendUrl;



    @GetMapping("/details-order-by-customer")
    @Operation(summary = "Getting order details", description = "Returning details of an order")
    public ResponseEntity<ApiResponse> getOrderByIdCustomer(@RequestParam UUID orderId) {
        OrderDetailResponseDto order = orderService.getOrderByIdCustomer(orderId);
        return ResponseEntity.ok(new ApiResponse("Found: ", order));
    }

    @PostMapping(value = "/place")
    @Operation(summary = "Adding order to a restaurant", description = "Making order request to a restaurant")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody AddOrderRequest request) {
        PlaceOrderDto result = orderService.addOrder(request);
        return ResponseEntity.ok(new ApiResponse("Order has been received, Please wait for the restaurant to confirm your order", result));
    }

    @PostMapping(value = "/add-payment-proof", consumes = {"multipart/form-data"})
    @Operation(summary = "Adding payment proof to an order id", description = "Paying for order")
    public ResponseEntity<ApiResponse> sendPayment(@RequestParam UUID orderId, @RequestParam MultipartFile paymentProof) {
        orderService.addPaymentProof(orderId, paymentProof);
        return ResponseEntity.ok(new ApiResponse("Payment proof received, please wait", null));
    }

    @GetMapping("/get-incoming-order")
    @Operation(summary = "Getting the list of incoming orders", description = "Returning list of incoming orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getIncomingOrders(@RequestParam UUID restaurantId) {
        User user = userService.getAuthenticatedUser();
        List<IncomingOrderDto> incomingOrderLists = orderService.getIncomingOrder(restaurantId);
        return ResponseEntity.ok(new ApiResponse("Successful", incomingOrderLists));
    }

    @GetMapping("/get-confirmed-order")
    @Operation(summary = "Getting the list of incoming orders", description = "Returning list of incoming orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getConfirmedOrders(@RequestParam UUID restaurantId) {
        User user = userService.getAuthenticatedUser();
        List<ConfirmedOrderDto> confirmedOrder = orderService.getConfirmedOrder(restaurantId);
        return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", confirmedOrder));
    }

    @GetMapping("/get-active")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    @Operation(summary = "Getting the list of active orders", description = "Returning list of active orders")
    public ResponseEntity<ApiResponse> getActiveOrders(@RequestParam UUID restaurantId) {
        User user = userService.getAuthenticatedUser();
        List<ActiveOrderDto> orderList = orderService.getActiveOrder(restaurantId);
        return ResponseEntity.ok(new ApiResponse("Getting list of active order is successful!", orderList));
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> confirmOrder(@RequestParam UUID orderId) {
        User user = userService.getAuthenticatedUser();
        orderService.confirmOrder(orderId, user.getId());
        return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", null));
    }

    @PostMapping("/confirm-payment")
    @Operation(summary = "Confirming an order payment of user", description = "Confirming order payment of user")
    public ResponseEntity<ApiResponse> confirmOrderPayment(@RequestParam UUID orderId) {
        User user = userService.getAuthenticatedUser();
        orderService.confirmOrderPayment(orderId, user.getId());
        return ResponseEntity.ok(new ApiResponse("Successful", null));
    }

    @PostMapping("/reject")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> rejectOrder(@RequestBody CancelOrderRequest request) {
        User user = userService.getAuthenticatedUser();
        String result = orderService.cancelOrderByRestaurant(request, user.getId());
        return ResponseEntity.ok(new ApiResponse("Order has been cancelled", result));
    }

    @PostMapping("/reject-payment")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<ApiResponse> rejectOrderPayment(@RequestBody RejectOrderPaymentDto request) {
        User user = userService.getAuthenticatedUser();
        orderService.rejectOrderPayment(request, user.getId());
        return ResponseEntity.ok(new ApiResponse("Order payment is rejected", "Order payment is rejected"));
    }

    @PostMapping("/finish")
    @Operation(summary = "Finishing an order", description = "Finishing user's order, done by restaurant")
    public ResponseEntity<ApiResponse> finishOrder(@RequestParam UUID orderId) {
        User user = userService.getAuthenticatedUser();
        String result = orderService.finishOrder(orderId, user.getId());
        return ResponseEntity.ok(new ApiResponse("Operation successful", result));
    }

    @PostMapping("/mark-ready")
    @Operation(summary = "Mark order as ready", description = "Mark an order as ready for pickup by restaurant")
    public ResponseEntity<ApiResponse> markOrderReady(@RequestParam UUID orderId) {
        User user = userService.getAuthenticatedUser();
        String result = orderService.markOrderReady(orderId, user.getId());
        return ResponseEntity.ok(new ApiResponse("Operation successful", result));
    }

    @GetMapping("/confirm-redirect")
    public ResponseEntity<String> confirmRedirect(@RequestParam UUID orderId) {
        String html = """
<html>
  <head>
    <title>Confirming Order...</title>
  </head>
  <body onload='document.forms[0].submit()'>
    <p>Processing your order... please wait.</p>
    <form method='POST' action='%sorder/confirm2'>
      <input type='hidden' name='orderId' value='%s'/>
    </form>
  </body>
</html>
""".formatted(backendUrl, orderId);


        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/confirm2")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    public ResponseEntity<String> confirmOrder2(@RequestParam UUID orderId) {
        try {
            String result = orderService.processOrderToActive(orderId);

            String html = """
                <html>
                  <head>
                    <title>Order Confirmed</title>
                    <script>
                      setTimeout(() => {
                        window.location.href = "%s/restaurant/management";
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
            log.error("Failed to confirm order {}: {}", orderId, e.getMessage());
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
        String result = orderService.generateOrderIdQrCode(orderId);
        return ResponseEntity.ok(new ApiResponse("Nice job, order id: ", result));
    }

    @GetMapping("/check-payment")
    @Operation(summary = "Checking order payment", description = "Checking order payment")
    public ResponseEntity<ApiResponse> checkOrderPayment(@RequestParam UUID orderId) {
        Boolean result = orderService.checkPayment(orderId);
        return ResponseEntity.ok(new ApiResponse("Order payment result: ", result));
    }

    @MessageMapping("/restaurant-recent-orders/request")
    public void handleRecentOrdersRequest(@Payload String restaurantIdStr) {
        try {
            UUID restaurantId = UUID.fromString(restaurantIdStr);

            if (!trackedRestaurantIds.contains(restaurantId)) {
                trackedRestaurantIds.add(restaurantId);
            }

            List<RecentOrderDto> recentOrders = orderService.getRecentOrderLists(restaurantId);
            messagingTemplate.convertAndSend(
                    "/topic/restaurant-recent-orders/" + restaurantIdStr,
                    recentOrders
            );

        } catch (Exception e) {
            log.error("WebSocket error for restaurant {}: {}", restaurantIdStr, e.getMessage());
        }
    }

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
                log.error("Failed to send recent orders for restaurant {}: {}", restaurantId, e.getMessage());
            }
        }
    }

    @MessageMapping("/order-progress/request")
    public void handleOrderRequest(@Payload String orderIdStr) {
        try {
            UUID orderId = UUID.fromString(orderIdStr);

            if (!trackedOrderIds.contains(orderId)) {
                trackedOrderIds.add(orderId);
            }

            ProgressOrderDto progress = orderRepository.getProgressOrder(orderId);
            messagingTemplate.convertAndSend("/topic/order-progress/" + orderId, progress);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid orderId format received: {}", orderIdStr);
        }
    }

    @GetMapping("/incoming")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    @Operation(summary = "Checking order payment", description = "Checking order payment")
    public ResponseEntity<ApiResponse> incoming(@RequestParam UUID restaurantId) {
        List<IncomingOrderDto> result = orderService.getIncomingOrder(restaurantId);
        return ResponseEntity.ok(new ApiResponse("Order payment result: ", result));
    }

    @GetMapping("/payment-modal-data")
    @Operation(summary = "Getting payment modal data", description = "Getting payment modal data")
    public ResponseEntity<ApiResponse> getPaymentModalData(@RequestParam UUID orderId) {
        PaymentModalDto result = orderService.getPaymentModalData(orderId);
        return ResponseEntity.ok(new ApiResponse("Payment modal data: ", result));
    }

    @Scheduled(fixedRate = 5000)
    public void sendPeriodicUpdates() {
        if (trackedOrderIds.isEmpty()) {
            return;
        }

        Set<UUID> ordersToRemove = new HashSet<>();

        for (UUID orderId : trackedOrderIds) {
            try {
                ProgressOrderDto progress = orderRepository.getProgressOrder(orderId);
                messagingTemplate.convertAndSend("/topic/order-progress/" + orderId, progress);
            } catch (RuntimeException e) {
                if (e.getMessage() != null && e.getMessage().contains("Incorrect result size")) {
                    Map<String, Object> notFoundPayload = new HashMap<>();
                    notFoundPayload.put("orderId", orderId.toString());
                    notFoundPayload.put("orderStatus", "NOT_FOUND");
                    notFoundPayload.put("message", "This order is no longer available. It may have been completed, cancelled, or expired.");

                    messagingTemplate.convertAndSend("/topic/order-progress/" + orderId, notFoundPayload);
                    ordersToRemove.add(orderId);
                }
            } catch (Exception e) {
                log.error("Failed to send order progress update for {}: {}", orderId, e.getMessage());
            }
        }

        if (!ordersToRemove.isEmpty()) {
            trackedOrderIds.removeAll(ordersToRemove);
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
                log.error("Failed to send restaurant order update for {}: {}", restaurantId, e.getMessage());
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

            List<IncomingOrderDto> incoming = orderService.getIncomingOrder(restaurantId);
            List<ConfirmedOrderDto> confirmed = orderService.getConfirmedOrder(restaurantId);
            List<ActiveOrderDto> active = orderService.getActiveOrder(restaurantId);

            Map<String, Object> payload = new HashMap<>();
            payload.put("incoming", incoming);
            payload.put("confirmed", confirmed);
            payload.put("active", active);

            messagingTemplate.convertAndSend("/topic/restaurant-orders/" + restaurantIdStr, payload);

        } catch (Exception e) {
            log.error("WebSocket error for restaurant orders {}: {}", restaurantIdStr, e.getMessage());
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
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        List<OrderHistoryDto> history = orderService.getCustomerOrderHistory(
                customerId,
                start,
                end,
                status
        );

        return ResponseEntity.ok(new ApiResponse("Order history retrieved successfully", history));
    }

    @GetMapping("/history/restaurant")
    @Operation(summary = "Get restaurant order history", description = "Retrieve order history for restaurant")
    public ResponseEntity<ApiResponse> getRestaurantOrderHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status
    ) {
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
    }

    @GetMapping("/reviewable")
    @Operation(summary = "Get reviewable orders", description = "Retrieve finished orders that haven't been reviewed yet for a specific restaurant")
    public ResponseEntity<ApiResponse> getReviewableOrders(
            @RequestParam String customerId,
            @RequestParam UUID restaurantId
    ) {
        List<OrderHistoryDto> orders = orderService.getReviewableOrders(customerId, restaurantId);
        return ResponseEntity.ok(new ApiResponse("Reviewable orders retrieved successfully", orders));
    }
}
