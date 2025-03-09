package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.model.Notification;

import java.util.List;
import java.util.UUID;

public interface INotificationService {
    void addOrderNotification(UUID orderId, UUID restaurantId, Long userId);
    void addRestaurantApprovalNotification(UUID approvalId, UUID restaurantId);
    List<Notification> getRestaurantNotification(UUID restaurantId, Long userId);
    List<Notification> getUserNotification(Long userId);
}
