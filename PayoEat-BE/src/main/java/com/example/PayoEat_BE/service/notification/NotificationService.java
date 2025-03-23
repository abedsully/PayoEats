package com.example.PayoEat_BE.service.notification;

import com.example.PayoEat_BE.dto.OrderNotificationDto;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final UserNotificationRepository userNotificationRepository;


    // Notification for restaurant when a user is ordering foods
    @Override
    public void addOrderNotification(UUID orderId, UUID restaurantId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        Notification restaurantNotification = new Notification();
        restaurantNotification.setOrderId(order.getId());
        restaurantNotification.setRequestTime(LocalTime.now());
        restaurantNotification.setRequestDate(LocalDate.now());
        restaurantNotification.setRestaurantId(restaurant.getId());
        restaurantNotification.setIsActive(true);

        notificationRepository.save(restaurantNotification);
    }

    //  Notification for admin when a user is creating a new restaurant (Approval)
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

    // Notification for restaurant to view the list of orders
    @Override
    public List<Notification> getOrderNotification(UUID restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!restaurant.getUserId().equals(user.getId())) {
            throw new InvalidException("User does not have access this restaurant's notification");
        }

        return notificationRepository.findByRestaurantIdAndIsActiveTrue(restaurant.getId());
    }

    // Notifications for users
    @Override
    public List<UserNotification> getUserNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        return userNotificationRepository.findAll();
    }

    // Adding a notification to user
    @Override
    public UserNotification addUserNotification(Long userId, UUID approvalId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        RestaurantApproval restaurantApproval = restaurantApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new NotFoundException("Restaurant approval not found with id: " + approvalId));


        UserNotification userNotification = new UserNotification();

        if (!restaurantApproval.getIsApproved() && restaurantApproval.getIsActive()) {
            userNotification.setMessage("We received your restaurant request: " + restaurantApproval.getRestaurantName() + " . Please wait for our admin to process your restaurant");
        } else {
            userNotification.setMessage("Our admin has processed your restaurant request, View here to see the result of your request");
        }

        userNotification.setApprovalId(restaurantApproval.getId());

        return userNotificationRepository.save(userNotification);
    }

    @Override
    public List<OrderNotificationDto> getConvertedOrderNotification(List<Notification> notificationList) {
        return notificationList.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderNotificationDto convertToDto(Notification notification) {
        return modelMapper.map(notification, OrderNotificationDto.class);
    }
}
