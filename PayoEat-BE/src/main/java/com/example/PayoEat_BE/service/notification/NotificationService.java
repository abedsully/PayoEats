package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.model.Notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class NotificationService implements INotificationService {
    @Override
    public Notification addOrderNotification(UUID orderId, String message) {
        Notification newOrderNotification = new Notification();
        newOrderNotification.setOrderMessage(message);
        newOrderNotification.setOrderId(orderId);
        newOrderNotification.setOrderDate(LocalDate.now());
        newOrderNotification.setOrderTime(LocalTime.now());
        return newOrderNotification;
    }
}
