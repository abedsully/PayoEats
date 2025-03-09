package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final RestaurantApprovalRepository restaurantApprovalRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public void addOrderNotification(UUID orderId, UUID restaurantId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Notification userNotification = new Notification();
        userNotification.setMessage("Order received");
        userNotification.setOrderId(order.getId());
        userNotification.setRequestTime(LocalTime.now());
        userNotification.setRequestDate(LocalDate.now());
        userNotification.setUserId(user.getId());

        Notification restaurantNotification = new Notification();
        restaurantNotification.setMessage("Order received");
        restaurantNotification.setOrderId(orderId);
        restaurantNotification.setRequestTime(LocalTime.now());
        restaurantNotification.setRequestDate(LocalDate.now());
        restaurantNotification.setRestaurantId(restaurant.getId());

        notificationRepository.save(userNotification);
        notificationRepository.save(restaurantNotification);
    }

    @Override
    public void addRestaurantApprovalNotification(UUID approvalId, UUID restaurantId) {

        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new NotFoundException("Restaurant approval request not found with id: " + approvalId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveFalse(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        Notification newApprovalNotification = new Notification();
        newApprovalNotification.setMessage("Incoming restaurant approval");
        newApprovalNotification.setRequestDate(LocalDate.now());
        newApprovalNotification.setRequestTime(LocalTime.now());
        newApprovalNotification.setApprovalId(restaurantApproval.getId());
        newApprovalNotification.setRestaurantId(restaurant.getId());

        notificationRepository.save(newApprovalNotification);
    }

    @Override
    public List<Notification> getRestaurantNotification(UUID restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!restaurant.getUserId().equals(user.getId())) {
            throw new InvalidException("User does not have access this restaurant's notification");
        }

        return notificationRepository.findByRestaurantIdAndIsActiveTrue(restaurant.getId());
    }

    @Override
    public List<Notification> getUserNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        return notificationRepository.findByUserId(user.getId());
    }
}
