package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Override
    public Order addOrder(AddOrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        validateMenuCodes(request.getMenuCode(), restaurant.getId());

        if (!user.getRoles().equals(UserRoles.CUSTOMER)) {
            throw new ForbiddenException("You need to be a customer to order foods");
        }

        return orderRepository.save(createOrder(request, user.getId()));
    }

    private void validateMenuCodes(List<UUID> menuCodes, UUID restaurantId) {
        menuCodes.forEach(menuCode ->
                menuRepository.findByMenuCodeAndRestaurantId(menuCode, restaurantId)
                        .orElseThrow(() -> new NotFoundException("Menu not found or inactive for code: " + menuCode + " in restaurant: " + restaurantId))
        );
    }

    @Override
    public List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!restaurant.getUserId().equals(user.getId()) && !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to view this restaurant's order");
        }

        return orderRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public void finishOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) && !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to finish this order");
        }

        order.setIsActive(false);
        orderRepository.save(order);
    }

    private Order createOrder(AddOrderRequest request, Long userId) {
        Order newRestaurantOrder = new Order();
        newRestaurantOrder.setMenuList(request.getMenuCode());
        newRestaurantOrder.setRestaurantId(request.getRestaurantId());

        String orderMessage = request.getOrderMessage();

        if (orderMessage == null || orderMessage.isEmpty()) {
            newRestaurantOrder.setOrderMessage(null);
        } else {
            newRestaurantOrder.setOrderMessage(orderMessage);
        }

        newRestaurantOrder.setOrderMessage(request.getOrderMessage());
        newRestaurantOrder.setUserId(userId);
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(true);

        return newRestaurantOrder;
    }
}
