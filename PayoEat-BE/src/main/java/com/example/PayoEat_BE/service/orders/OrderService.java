package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.dto.IncomingOrderDto;
import com.example.PayoEat_BE.dto.MenuListDto;
import com.example.PayoEat_BE.dto.RestaurantStatusDto;
import com.example.PayoEat_BE.repository.*;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import com.example.PayoEat_BE.utils.QrCodeUtil;
import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
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
    private final OrderItemRepository orderItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final IImageService imageService;


    @Override
    public String generateOrderIdQrCode(UUID orderId) {
        String qrCode = QrCodeUtil.generateBase64Qr(orderId.toString(), 200, 200);

        return qrCode;
    }


    // Flow pertama, user add order
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

    // Flow 2, restaurant confirm order (CASE YES)
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

        if (!order.getOrderStatus().equals(OrderStatus.RECEIVED)) {
            throw new IllegalArgumentException("Unable to confirm this order because order status is not received");
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


    // Flow 2, Restaurant confirm (NO)
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

        if (!order.getOrderStatus().equals(OrderStatus.RECEIVED)) {
            throw new IllegalArgumentException("This order can't be cancelled");
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


        if (!order.getOrderStatus().equals(OrderStatus.RECEIVED)) {
            throw new IllegalArgumentException("You can't cancel this order, as the order has already been processed");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setIsActive(false);
        order.setCancellationReason(request.getCancellationReason());

        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    @Override
    public Order getOrderDetail(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
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

        if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("Unable to process order, order has not been confirmed yet");
        }

        order.setOrderStatus(OrderStatus.DINING);
        orderRepository.save(order);

        return "Order is processed";
    }

    @Override
    public RestaurantStatusDto restaurantOrderStatus(LocalDate date, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        Restaurant restaurant = restaurantRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with user id: " + userId));


        if (!restaurant.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        long activeOrders = 0L;
        long completedOrders = 0L;
        long totalOrders = 0L;
        Double totalIncome = 0.0;

        List<Order> activeOrdersLists = orderRepository.findByRestaurantIdAndCreatedDateAndOrderStatusAndIsActiveTrue(restaurant.getId(), date, OrderStatus.DINING);
        List<Order> completedOrderLists = orderRepository.findByRestaurantIdAndCreatedDateAndOrderStatusAndIsActiveFalse(restaurant.getId(), date, OrderStatus.FINISHED);

        activeOrders = (long) activeOrdersLists.size();
        completedOrders = (long) completedOrderLists.size();
        totalOrders = activeOrders + completedOrders;

        for (Order order : activeOrdersLists) {
            totalIncome += order.getTotalPrice();
        }

        for (Order order : completedOrderLists) {
            totalIncome += order.getTotalPrice();
        }

        RestaurantStatusDto result = new RestaurantStatusDto();
        result.setActiveOrders(activeOrders);
        result.setCompletedOrders(completedOrders);
        result.setTotalOrders(totalOrders);
        result.setTotalIncome(totalIncome);

        return result;
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

        if (!order.getOrderStatus().equals(OrderStatus.DINING)) {
            throw new IllegalArgumentException("This order can't be finished, the customer hasn't dined in yet");
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

    @Override
    public List<OrderItem> getOrderItems(UUID orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public List<IncomingOrderDto> getIncomingOrder(UUID restaurantId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        if (!user.getId().equals(restaurant.getUserId()) || user.getRoles() != UserRoles.RESTAURANT) {
            throw new ForbiddenException("Sorry you don't have access to view this order");
        }

        List<Order> orderList = orderRepository
                .findByRestaurantIdAndCreatedDateAndOrderStatusAndIsActiveTrue(
                        restaurantId, LocalDate.now(), OrderStatus.RECEIVED
                );

        List<IncomingOrderDto> incomingOrders = new ArrayList<>();

        for (Order o : orderList) {
            IncomingOrderDto dto = new IncomingOrderDto();
            dto.setRestaurantId(restaurantId);
            dto.setOrderId(o.getId());

            List<OrderItem> orderItems = getOrderItems(o.getId());

            List<MenuListDto> menuDtos = new ArrayList<>();
            for (OrderItem oi : orderItems) {
                MenuListDto menuDto = new MenuListDto();
                menuDto.setMenuCode(oi.getMenuCode());
                menuDto.setQuantity(oi.getQuantity());

                menuRepository.findByMenuCodeAndIsActiveTrue(oi.getMenuCode()).ifPresent(menu -> {
                    menuDto.setMenuName(menu.getMenuName());
                    menuDto.setMenuPrice(menu.getMenuPrice());
                    menuDto.setMenuImage(menu.getMenuImageUrl());
                    menuDto.setTotalPrice(menu.getMenuPrice() * oi.getQuantity());
                });

                menuDtos.add(menuDto);
            }

            dto.setMenuLists(menuDtos);
            incomingOrders.add(dto);
        }

        return incomingOrders;
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

        double subTotalPrice = 0.0;
        double taxPrice = 0.0;
        double totalPrice = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Menu menu = menuRepository.findByMenuCodeAndIsActiveTrue(itemRequest.getMenuCode())
                    .orElseThrow(() -> new NotFoundException("Menu not found"));

            double itemTotalPrice = menu.getMenuPrice() * itemRequest.getQuantity();
            subTotalPrice += itemTotalPrice;

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuCode(itemRequest.getMenuCode());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(newRestaurantOrder);

            orderItems.add(orderItem);
        }

        taxPrice = (subTotalPrice * restaurant.getTax()) / 100;
        totalPrice = subTotalPrice + taxPrice;


        newRestaurantOrder.setMenuLists(orderItems);
        newRestaurantOrder.setCreatedDate(LocalDate.now());
        newRestaurantOrder.setCreatedTime(LocalTime.now());
        newRestaurantOrder.setIsActive(true);


        // Area setting up numbers for price
        newRestaurantOrder.setSubTotal(subTotalPrice);
        newRestaurantOrder.setTaxPrice(taxPrice);
        newRestaurantOrder.setTotalPrice(totalPrice);

        newRestaurantOrder.setOrderStatus(OrderStatus.RECEIVED);
        newRestaurantOrder.setQuotas(request.getQuotas());
        newRestaurantOrder.setDineInTime(request.getDineInTime());

        orderRepository.save(newRestaurantOrder);

        return newRestaurantOrder;
    }

}
