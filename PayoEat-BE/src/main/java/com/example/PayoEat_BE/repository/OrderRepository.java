package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByRestaurantId(UUID restaurantId);
    Optional<Order> findByIdAndIsActiveTrue(UUID id);
    Optional<Order> findByIdAndIsActiveFalse(UUID id);
    Optional<List<Order>> findByRestaurantIdAndIsActiveTrue(UUID restaurantId);
}
