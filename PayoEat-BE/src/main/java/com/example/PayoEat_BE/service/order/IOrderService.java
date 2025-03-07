package com.example.PayoEat_BE.service.order;

import com.example.PayoEat_BE.model.RestaurantOrder;
import com.example.PayoEat_BE.request.order.AddOrderRequest;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    RestaurantOrder addOrder(AddOrderRequest request, Long userId);
    List<RestaurantOrder> getOrderByRestaurantId(UUID restaurantId, Long userId);
}
