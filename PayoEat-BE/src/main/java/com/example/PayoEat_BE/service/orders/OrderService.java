package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.OrderItemRequest;
import com.example.PayoEat_BE.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final IImageService imageService;

    @Override
    public Order addOrder(AddOrderRequest request) {

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        validateMenuCodes(request.getItems(), restaurant.getId());


        return orderRepository.save(createOrder(request));
    }

    private void validateMenuCodes(List<OrderItemRequest> orderItems, UUID restaurantId) {
        orderItems.forEach(item ->
                menuRepository.findByMenuCodeAndRestaurantId(item.getMenuCode(), restaurantId)
                        .orElseThrow(() -> new NotFoundException("Menu not found or inactive for code: " + item.getMenuCode() + " in restaurant: " + restaurantId))
        );
    }

    @Override
    public Order addPaymentProof(UUID orderId, MultipartFile paymentProof) {
        Order order = orderRepository.findByIdAndIsActiveFalse(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Image imagePaymentProof = imageService.savePaymentProofImage(paymentProof, order.getId());
        imagePaymentProof.setOrderId(order.getId());

        order.setPaymentImage(imagePaymentProof.getId());

        return orderRepository.save(order);

    }

    @Override
    public List<Order> getOrderByRestaurantId(UUID restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to view this restaurant's order");
        }

        return orderRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public Order confirmOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveFalse(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to confirm this order");
        }

        order.setIsActive(true);


        return orderRepository.save(order);
    }

    @Override
    public List<Order> viewActiveOrders(UUID restaurantId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to view this order");
        }

        return orderRepository.findByRestaurantIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public Order finishOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to finish this order");
        }

        order.setIsActive(false);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(UUID orderId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));


        return order;
    }

    private Order createOrder(AddOrderRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        double taxFee = restaurant.getTaxFee();

        Order newRestaurantOrder = new Order();
        newRestaurantOrder.setRestaurantId(restaurant.getId());

        String orderMessage = request.getOrderMessage();
        if (orderMessage == null || orderMessage.isEmpty()) {
            newRestaurantOrder.setOrderMessage(null);
        } else {
            newRestaurantOrder.setOrderMessage(orderMessage);
        }

        double totalPrice = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Menu menu = menuRepository.findByMenuCodeAndIsActiveTrue(itemRequest.getMenuCode())
                    .orElseThrow(() -> new NotFoundException("Menu not found"));

            double itemTotalPrice = menu.getMenuPrice() * itemRequest.getQuantity();
            totalPrice += itemTotalPrice;

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuCode(itemRequest.getMenuCode());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(newRestaurantOrder);

            orderItems.add(orderItem);
        }

        taxFee = (totalPrice * taxFee) / 100;
        totalPrice += taxFee;

        newRestaurantOrder.setMenuLists(orderItems);
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(false);
        newRestaurantOrder.setTotalAmount(totalPrice);



        orderRepository.save(newRestaurantOrder);

        return newRestaurantOrder;
    }

}
