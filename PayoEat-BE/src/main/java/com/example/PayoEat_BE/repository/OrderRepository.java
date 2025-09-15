package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByRestaurantId(UUID restaurantId);
    Optional<Order> findByIdAndIsActiveTrue(UUID id);
    Optional<Order> findByIdAndIsActiveFalse(UUID id);
    Optional<List<Order>> findByRestaurantIdAndIsActiveTrue(UUID restaurantId);

    // Completed Orders
    List<Order> findByRestaurantIdAndCreatedDateAndOrderStatusAndIsActiveFalse(UUID restaurantId, LocalDate date, OrderStatus orderStatus);

    // Active Orders
    List<Order> findByRestaurantIdAndCreatedDateAndOrderStatusAndIsActiveTrue(UUID restaurantId, LocalDate date, OrderStatus orderStatus);

}
