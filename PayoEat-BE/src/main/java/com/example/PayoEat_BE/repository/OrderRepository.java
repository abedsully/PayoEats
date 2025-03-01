package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByRestaurantId(UUID restaurantId);
}
