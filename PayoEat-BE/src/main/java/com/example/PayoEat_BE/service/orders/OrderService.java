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

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Override
    public Order addOrder(AddOrderRequest request) {

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        validateMenuCodes(request.getMenuCode(), restaurant.getId());


        return orderRepository.save(createOrder(request));
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
    public Order confirmOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) && !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to confirm this order");
        }



        return orderRepository.save(order);
    }

    @Override
    public Order finishOrder(UUID orderId, Long userId) {
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
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    }

    private Order createOrder(AddOrderRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        double taxFee = restaurant.getTaxFee();

        Order newRestaurantOrder = new Order();
        newRestaurantOrder.setMenuList(request.getMenuCode());
        newRestaurantOrder.setRestaurantId(restaurant.getId());

        String orderMessage = request.getOrderMessage();

        if (orderMessage == null || orderMessage.isEmpty()) {
            newRestaurantOrder.setOrderMessage(null);
        } else {
            newRestaurantOrder.setOrderMessage(orderMessage);
        }

        double totalPrice = 0.0;

        for (UUID menuCode : request.getMenuCode()) {
            Menu menu = menuRepository.findByMenuCodeAndIsActiveTrue(menuCode)
                    .orElseThrow(() -> new NotFoundException("Menu not found"));

            totalPrice += menu.getMenuPrice();
        }

        taxFee = (totalPrice * taxFee) / 100;

        totalPrice += taxFee;

        newRestaurantOrder.setOrderMessage(request.getOrderMessage());
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(true);
        newRestaurantOrder.setTotalAmount(totalPrice);

        orderRepository.save(newRestaurantOrder);

        return newRestaurantOrder;
    }
}
