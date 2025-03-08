package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.Restaurant;
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

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public Order addOrder(AddOrderRequest request, Long userId) {
        return orderRepository.save(createOrder(request, userId));
    }

    @Override
    public List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        if (!restaurant.getUserId().equals(userId)) {
            throw new ForbiddenException("User does not have access to view this restaurant's order");
        }

        return orderRepository.findByRestaurantId(restaurant.getId());
    }

    private Order createOrder(AddOrderRequest request, Long userId) {
        Order newRestaurantOrder = new Order();
        newRestaurantOrder.setMenuList(request.getMenuCode());
        newRestaurantOrder.setRestaurantId(request.getRestaurantId());
        newRestaurantOrder.setUserId(userId);
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(true);

        return newRestaurantOrder;
    }
}
