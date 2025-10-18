package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
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

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse> getOrder(@RequestParam UUID orderId) {
        try {
            ProgressOrderDto result = orderService.getProgressOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Success viewing order progress", result));
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

    @GetMapping("/get-incoming-order")
    @Operation(summary = "Getting the list of incoming orders", description = "Returning list of incoming orders")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getIncomingOrders(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<IncomingOrderDto> incomingOrderLists = orderService.getIncomingOrder(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", incomingOrderLists));
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
            List<ConfirmedOrderDto> confirmedOrder = orderService.getConfirmedOrder(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", confirmedOrder));
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
            List<ActiveOrderDto> orderList = orderService.getActiveOrder(restaurantId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Getting list of active order is successful!", orderList));
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

    @PostMapping("/confirm-payment")
    @Operation(summary = "Confirming an order payment of user", description = "Confirming order payment of user")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> confirmOrderPayment(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            Order order = orderService.confirmOrderPayment(orderId, user.getId());
            return ResponseEntity.ok(new ApiResponse("Order confirmed, Please directly process this order!", order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/reject")
    @Operation(summary = "Confirming an order made by user", description = "Confirming order request from user")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
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
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
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
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> finishOrder(@RequestParam UUID orderId) {
        try {
            User user = userService.getAuthenticatedUser();
            String result = orderService.finishOrder(orderId, user.getId());
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
            String result = orderService.processOrder(orderId);

            // HTML with JS redirect
            String html = """
                <html>
                  <head>
                    <title>Order Confirmed</title>
                    <script>
                      // Redirect after 1 second
                      setTimeout(() => {
                        window.location.href = 'http://localhost:5173/dashboard';
                      }, 1000);
                    </script>
                  </head>
                  <body>
                    <h1>Order confirmed!</h1>
                    <p>Redirecting to dashboard...</p>
                  </body>
                </html>
                """;

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

}
