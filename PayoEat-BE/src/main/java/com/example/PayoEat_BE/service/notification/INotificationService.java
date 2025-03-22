package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.dto.OrderNotificationDto;
import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.model.Notification;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.UserNotification;

import java.util.List;
import java.util.UUID;

public interface INotificationService {
    void addOrderNotification(UUID orderId, UUID restaurantId);
    void addRestaurantApprovalNotification(UUID approvalId, UUID restaurantId);
    List<Notification> getRestaurantNotification(UUID restaurantId, Long userId);
    List<Notification> getUserNotification(Long userId);
    List<OrderNotificationDto> getConvertedOrderNotification(List<Notification> notificationList);
    OrderNotificationDto convertToDto(Notification notification);
    UserNotification addUserNotification(Long userId, UUID approvalId);

}
