package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.model.Notification;

import java.util.UUID;

public interface INotificationService {
    Notification addOrderNotification(UUID orderId, String message);
//    Notification addRestaurantApprovalNotification()
}
