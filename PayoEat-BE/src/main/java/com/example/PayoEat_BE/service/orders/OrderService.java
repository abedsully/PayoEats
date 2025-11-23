package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.enums.PaymentStatus;
import com.example.PayoEat_BE.exceptions.InvalidException;
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

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderRepositoryy orderRepositoryy;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final IImageService imageService;


    @Override
    public String generateOrderIdQrCode(UUID orderId) {
        return QrCodeUtil.generateBase64Qr(orderId.toString(), 200, 200);
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

    public boolean checkOrderStatus(Order order) {
        LocalDateTime paymentBeginAt = order.getPaymentBeginAt();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(paymentBeginAt, now);

        if (duration.toMinutes() > 10) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.EXPIRED);
            orderRepository.save(order);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Order addPaymentProof(UUID orderId, MultipartFile paymentProof) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.PAYMENT) {
            throw new InvalidException("Unable to add payment proof as the order status is not PAYMENT");
        }

        if (!checkOrderStatus(order) && order.getPaymentImage() == null) {
            throw new InvalidException("This order has been cancelled, it has crossed 10 minutes");
        }

        Image imagePaymentProof = imageService.savePaymentProofImage(paymentProof, order.getId());
        imagePaymentProof.setOrderId(order.getId());

        order.setPaymentImage(imagePaymentProof.getId());
        order.setPaymentStatus(PaymentStatus.UPLOADED);

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

    // Flow 2, restaurant confirm order, masuk ke payment
    @Override
    public Order confirmOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
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

        order.setOrderStatus(OrderStatus.PAYMENT);
        order.setPaymentStatus(PaymentStatus.PENDING);

        order.setPaymentBeginAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // Flow tambahan order
    @Override
    public Order confirmOrderPayment(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to confirm this order");
        }

        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT) || order.getPaymentImage() == null) {
            throw new IllegalArgumentException("Unable to confirm this order payment");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.APPROVED);

        return orderRepository.save(order);
    }

    @Override
    public void rejectOrderPayment(RejectOrderPaymentDto dto, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(dto.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + dto.getOrderId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to confirm this order");
        }

        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT) || order.getPaymentImage() == null) {
            throw new IllegalArgumentException("Unable to reject this order payment");
        }

        order.setPaymentImage(null);
        order.setPaymentImageRejectionReason(dto.getRejectionReason());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentBeginAt(LocalDateTime.now());
        Long countPaymentImageRejection = order.getPaymentImageRejectionCount();

        if (countPaymentImageRejection == null) {
            countPaymentImageRejection = 1L;
        } else {
            countPaymentImageRejection += 1L;
        }

        // Automatically cancel the order because it has been rejected 3 times
        if (countPaymentImageRejection > 2L) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.EXPIRED);
        }

        order.setPaymentImageRejectionCount(countPaymentImageRejection);

        orderRepository.save(order);
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


        if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
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
    public String processOrder(UUID orderId) {

        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("Unable to process order, order has not been confirmed yet");
        }

        order.setOrderStatus(OrderStatus.ACTIVE);
        order.setDineInTime(LocalTime.now());
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

        List<Order> activeOrdersLists = orderRepository.findByRestaurantIdAndCreatedDateAndOrderStatusInAndIsActiveTrue(restaurant.getId(), date, List.of(OrderStatus.ACTIVE));
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
    public ProgressOrderDto getProgressOrder(UUID orderId) {
        return orderRepositoryy.getProgressOrder(orderId);
    }

    @Override
    public Boolean checkPayment(UUID orderId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT)) {
            throw new IllegalArgumentException("This order payment can not be checked as the order status is invalid");
        }

        return orderRepositoryy.checkPayment(order.getId());
    }

    @Override
    public String finishOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + order.getRestaurantId()));

        if (!restaurant.getUserId().equals(user.getId()) || !user.getRoles().equals(UserRoles.RESTAURANT)) {
            throw new ForbiddenException("User does not have access to finish this order");
        }

        if (!order.getOrderStatus().equals(OrderStatus.ACTIVE)) {
            throw new IllegalArgumentException("This order can't be finished, the customer hasn't dined in yet");
        }

        order.setIsActive(false);
        order.setOrderStatus(OrderStatus.FINISHED);
        orderRepository.save(order);

        return "This order has been completed";
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
    public List<IncomingOrderDto> getIncomingOrder(UUID restaurantId) {


        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));


        List<Order> orderList = orderRepository
                .findByRestaurantIdAndCreatedDateAndOrderStatusInAndIsActiveTrue(
                        restaurantId, LocalDate.now(), List.of(OrderStatus.RECEIVED)
                );

        List<IncomingOrderDto> incomingOrders = new ArrayList<>();

        for (Order o : orderList) {
            IncomingOrderDto dto = new IncomingOrderDto();
            dto.setRestaurantId(restaurantId);
            dto.setOrderId(o.getId());

            List<OrderItem> orderItems = getOrderItems(o.getId());

            List<MenuListDto> menuDtos = new ArrayList<>();
            double subtotal = 0.0;
            double orderTotalPrice = 0.0;
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

                if (menuDto.getTotalPrice() != null) {
                    subtotal += menuDto.getTotalPrice();
                }

                menuDtos.add(menuDto);
            }

            double tax = subtotal * 0.1;
            orderTotalPrice = subtotal + tax;
            dto.setTotalPrice(orderTotalPrice);
            dto.setSubTotal(subtotal);
            dto.setTaxPrice(tax);
            dto.setReceivedAt(o.getOrderTime());
            dto.setMenuLists(menuDtos);
            incomingOrders.add(dto);
        }

        return incomingOrders;
    }

    @Override
    public List<ConfirmedOrderDto> getConfirmedOrder(UUID restaurantId) {



        List<Order> orderList = orderRepository
                .findByRestaurantIdAndCreatedDateAndOrderStatusInAndIsActiveTrue(
                        restaurantId, LocalDate.now(), List.of(OrderStatus.PAYMENT)
                );

        List<ConfirmedOrderDto> confirmedOrderDtos = new ArrayList<>();

        for (Order o : orderList) {
            ConfirmedOrderDto dto = new ConfirmedOrderDto();
            dto.setRestaurantId(restaurantId);
            dto.setOrderId(o.getId());
            dto.setPaymentImage(o.getPaymentImage());

            List<OrderItem> orderItems = getOrderItems(o.getId());

            List<MenuListDto> menuDtos = new ArrayList<>();
            double subtotal = 0.0;
            double orderTotalPrice = 0.0;
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

                if (menuDto.getTotalPrice() != null) {
                    subtotal += menuDto.getTotalPrice();
                }

                menuDtos.add(menuDto);
            }

            double tax = subtotal * 0.1;
            orderTotalPrice = subtotal + tax;
            dto.setTotalPrice(orderTotalPrice);
            dto.setSubTotal(subtotal);
            dto.setTaxPrice(tax);

            dto.setMenuLists(menuDtos);
            confirmedOrderDtos.add(dto);
        }

        return confirmedOrderDtos;
    }

    @Override
    public List<ActiveOrderDto> getActiveOrder(UUID restaurantId) {


        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));


        List<Order> orderList = orderRepository.findByRestaurantIdAndCreatedDateAndOrderStatusInAndIsActiveTrue(restaurantId, LocalDate.now(), List.of(OrderStatus.ACTIVE, OrderStatus.CONFIRMED));

        List<ActiveOrderDto> activeOrderDtos = new ArrayList<>();

        for (Order o : orderList) {
            ActiveOrderDto dto = new ActiveOrderDto();
            dto.setRestaurantId(restaurantId);
            dto.setOrderId(o.getId());
            dto.setDineInTime(o.getDineInTime());

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
            activeOrderDtos.add(dto);
        }

        return activeOrderDtos;
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
        newRestaurantOrder.setOrderTime(ZonedDateTime.now());
        newRestaurantOrder.setIsActive(true);


        // Area setting up numbers for price
        newRestaurantOrder.setSubTotal(subTotalPrice);
        newRestaurantOrder.setTaxPrice(taxPrice);
        newRestaurantOrder.setTotalPrice(totalPrice);
        newRestaurantOrder.setOrderStatus(OrderStatus.RECEIVED);
        newRestaurantOrder.setQuotas(request.getQuotas());
        newRestaurantOrder.setDineInTime(null);
        newRestaurantOrder.setCreatedDate(LocalDate.now());

        orderRepository.save(newRestaurantOrder);

        return newRestaurantOrder;
    }

}
