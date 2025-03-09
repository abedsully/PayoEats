package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRestaurantIdAndIsActiveTrue(UUID restaurantId);
    List<Notification> findByUserId(Long userId);
}
