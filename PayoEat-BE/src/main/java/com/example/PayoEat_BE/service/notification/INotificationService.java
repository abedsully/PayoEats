package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.dto.OrderNotificationDto;
import com.example.PayoEat_BE.model.Notification;
import com.example.PayoEat_BE.model.UserNotification;

import java.util.List;
import java.util.UUID;

public interface INotificationService {
    void addOrderNotification(UUID orderId, UUID restaurantId);
    void addRestaurantApprovalNotification(UUID approvalId, UUID restaurantId);
    List<Notification> getOrderNotification(UUID restaurantId, Long userId);
    List<UserNotification> getUserNotification(Long userId);
    List<OrderNotificationDto> getConvertedOrderNotification(List<Notification> notificationList);
    OrderNotificationDto convertToDto(Notification notification);
    UserNotification addUserNotification(Long userId, UUID approvalId);

}
