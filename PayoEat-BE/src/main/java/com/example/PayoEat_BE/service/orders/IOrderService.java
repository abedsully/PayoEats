package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.request.order.AddOrderRequest;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    Order addOrder(AddOrderRequest request);
    List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId);
    Order confirmOrder(UUID orderId, Long userId);
    Order finishOrder(UUID orderId, Long userId);
    Order getOrderById(UUID orderId, Long userId);
}
