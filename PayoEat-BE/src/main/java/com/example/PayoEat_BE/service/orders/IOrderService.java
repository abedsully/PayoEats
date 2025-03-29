package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    Order addOrder(AddOrderRequest request);
    Order addPaymentProof(UUID orderId, MultipartFile paymentProof);
    List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId);
    Order confirmOrder(UUID orderId, Long userId);
    Order finishOrder(UUID orderId, Long userId);
    Order getOrderByIdRestaurant(UUID orderId, Long userId);
    Order getOrderByIdCustomer(UUID orderId);
    List<Order> viewActiveOrders(UUID restaurantId, Long userId);
}
