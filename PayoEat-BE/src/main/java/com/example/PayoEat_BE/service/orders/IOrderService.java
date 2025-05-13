package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    String nice(UUID orderId);
    Order addOrder(AddOrderRequest request);
    Order addPaymentProof(UUID orderId, MultipartFile paymentProof);
    List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId);
    Order confirmOrder(UUID orderId, Long userId);
    Order finishOrder(UUID orderId, Long userId);
    Order getOrderByIdRestaurant(UUID orderId, Long userId);
    Order getOrderByIdCustomer(UUID orderId);
    List<Order> viewActiveOrders(UUID restaurantId, Long userId);
    String cancelOrderByRestaurant(CancelOrderRequest request, Long userId);
    String cancelOrderByCustomer(CancelOrderRequest request);

    String processOrder(UUID orderId, Long userId);
}
