package com.example.PayoEat_BE.service.order;

import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.request.order.AddOrderRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderService {
    Order addOrder(AddOrderRequest request, Long userId);
    List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId);
}
