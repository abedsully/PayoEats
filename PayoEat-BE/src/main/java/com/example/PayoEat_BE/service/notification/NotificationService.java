package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.model.Notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class NotificationService implements INotificationService {
    @Override
    public Notification addOrderNotification(UUID orderId) {
        Notification newOrderNotification = new Notification();
        newOrderNotification.setMessage("Order received");
        newOrderNotification.setOrderId(orderId);
        newOrderNotification.setRequestTime(LocalTime.now());
        newOrderNotification.setRequestDate(LocalDate.now());
        return newOrderNotification;
    }

    @Override
    public Notification addRestaurantApprovalNotification(UUID approvalId) {
        Notification newApprovalNotification = new Notification();
        newApprovalNotification.setMessage("Incoming restaurant approval");
        newApprovalNotification.setRequestDate(LocalDate.now());
        newApprovalNotification.setRequestTime(LocalTime.now());
        newApprovalNotification.setApprovalId(approvalId);

        return newApprovalNotification;
    }
}
