package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import com.example.PayoEat_BE.utils.QrCodeUtil;
import com.example.PayoEat_BE.enums.OrderStatus;
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
    public String nice(UUID orderId) {
        String qrCode = QrCodeUtil.generateBase64Qr(orderId.toString(), 200, 200);

        return qrCode;
    }

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

        order.setOrderStatus(OrderStatus.CONFIRMED);

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
    public String cancelOrderByRestaurant(CancelOrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Order order = orderRepository.findByIdAndIsActiveTrue(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + request.getOrderId()));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to view this order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setIsActive(false);
        order.setCancellationReason(request.getCancellationReason());

        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    @Override
    public String cancelOrderByCustomer(CancelOrderRequest request) {
        Order order = orderRepository.findByIdAndIsActiveTrue(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + request.getOrderId()));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setIsActive(false);
        order.setCancellationReason(request.getCancellationReason());

        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    @Override
    public String processOrder(UUID orderId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to view this order");
        }

        order.setOrderStatus(OrderStatus.DINING);
        orderRepository.save(order);

        return "Order is processed";
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
        order.setOrderStatus(OrderStatus.FINISHED);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByIdRestaurant(UUID orderId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!user.getId().equals(restaurant.getUserId()) || user.getRoles() != UserRoles.RESTAURANT) {
            throw new ForbiddenException("Sorry you don't have access to view this order");
        }

        return order;
    }

    @Override
    public Order getOrderByIdCustomer(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        return order;
    }

    private Order createOrder(AddOrderRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));


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

        newRestaurantOrder.setMenuLists(orderItems);
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(true);
        newRestaurantOrder.setTotalAmount(totalPrice);
        newRestaurantOrder.setOrderStatus(OrderStatus.RECEIVED);

        orderRepository.save(newRestaurantOrder);

        return newRestaurantOrder;
    }

}
