package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.dto.orders.*;
import com.example.PayoEat_BE.dto.restaurants.CheckUserRestaurantDto;
import com.example.PayoEat_BE.dto.restaurants.TodayRestaurantStatusDto;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.repository.*;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import com.example.PayoEat_BE.service.UploadService;
import com.example.PayoEat_BE.utils.QrCodeUtil;
import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.UploadType;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.*;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.OrderItemRequest;
import com.example.PayoEat_BE.utils.UserAccessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserAccessValidator userAccessValidator;
    private final UploadService uploadService;
    private final QrCodeUtil qrCodeUtil;

    @Override
    public String generateOrderIdQrCode(UUID orderId) {
        return qrCodeUtil.generateBase64Qr(orderId.toString(), 200, 200);
    }

    private void checkIfRestaurantExists(UUID restaurantId) {
        restaurantRepository.findRestaurantByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant is not found"));
    }

    // Flow pertama, user add order
    @Override
    public PlaceOrderDto addOrder(AddOrderRequest request) {

        checkIfRestaurantExists(request.getRestaurantId());

        validateMenuCodes(request.getItems(), request.getRestaurantId());

        return createOrder(request);
    }

    private void validateMenuCodes(List<OrderItemRequest> orderItems, UUID restaurantId) {
        orderItems.forEach(item ->
                menuRepository.findByMenuCodeAndRestaurantId(item.getMenuCode(), restaurantId)
                        .orElseThrow(() -> new NotFoundException("Menu not found or inactive for code: " + item.getMenuCode() + " in restaurant: " + restaurantId))
        );
    }

    @Override
    public void addPaymentProof(UUID orderId, MultipartFile file) {
        CheckOrderDto order = checkOrderExistance(orderId);

        if (order.getOrderStatus() != OrderStatus.PAYMENT) {
            throw new InvalidException("Unable to add payment proof as the order status is not PAYMENT");
        }

        String url = uploadService.upload(file, UploadType.PAYMENT);

        orderRepository.addPaymentProof(order.getId(), url);
    }

    // Flow 2, restaurant confirm order, masuk ke payment
    @Override
    public void confirmOrder(UUID orderId, Long userId) {
        checkUserRestaurant(userId);
        CheckOrderDto order = checkOrderExistance(orderId);
        checkIfRestaurantExists(order.getRestaurantId());

        if (!order.getOrderStatus().equals(OrderStatus.RECEIVED)) {
            throw new IllegalArgumentException("Unable to confirm this order because order status is not received");
        }


        orderRepository.processOrderToPayment(order.getId());
    }

    // Flow tambahan order
    @Override
    public void confirmOrderPayment(UUID orderId, Long userId) {
        CheckOrderDto order = checkOrderExistance(orderId);
        checkIfRestaurantExists(order.getRestaurantId());

        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT) || order.getPaymentImageUrl().isBlank() || order.getPaymentImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Unable to confirm this order payment");
        }

        orderRepository.confirmOrderPayment(order.getId());
    }

    @Override
    public void rejectOrderPayment(RejectOrderPaymentDto dto, Long userId) {
        checkUserRestaurant(userId);
        CheckOrderDto order = checkOrderExistance(dto.getOrderId());
        checkIfRestaurantExists(order.getRestaurantId());


        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT) || order.getPaymentImageUrl().isBlank() || order.getPaymentImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Unable to reject this order payment");
        }

        Long countPaymentImageRejection = order.getPaymentImageRejectionCount();

        if (countPaymentImageRejection == null) {
            countPaymentImageRejection = 1L;
        } else {
            countPaymentImageRejection += 1L;
        }

        if (countPaymentImageRejection > 2L) {
            orderRepository.cancelOrder(order.getId(), "Payment limit has exceeded");
            return;
        }

        orderRepository.rejectOrderPayment(order.getId(), dto.getRejectionReason(), countPaymentImageRejection);
    }

    // Flow 2, Restaurant confirm (NO)
    @Override
    public String cancelOrderByRestaurant(CancelOrderRequest request, Long userId) {
        checkUserRestaurant(userId);
        CheckOrderDto order = checkOrderExistance(request.getOrderId());
        checkIfRestaurantExists(order.getRestaurantId());

        if (!order.getOrderStatus().equals(OrderStatus.RECEIVED)) {
            throw new IllegalArgumentException("This order can't be cancelled");
        }

        if (request.getCancellationReason().isEmpty() || order.getPaymentImageUrl().isBlank()) {
            throw new IllegalArgumentException("Please provide cancellation reason");
        }

        orderRepository.cancelOrder(order.getId(), request.getCancellationReason());

        return "Order cancelled successfully";
    }

    @Override
    public String cancelOrderByCustomer(CancelOrderRequest request) {
        CheckOrderDto order = checkOrderExistance(request.getOrderId());

        checkIfRestaurantExists(order.getRestaurantId());

        if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("You can't cancel this order, as the order has already been processed");
        }

        orderRepository.cancelOrder(order.getId(), request.getCancellationReason());

        return "Order cancelled successfully";
    }


    @Override
    public String processOrderToActive(UUID orderId) {
        CheckOrderDto order = checkOrderExistance(orderId);
        checkIfRestaurantExists(order.getRestaurantId());

        if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("Unable to process order, order has not been confirmed yet");
        }

        orderRepository.processOrderToActive(order.getId());

        return "Order is processed";
    }

    private CheckUserRestaurantDto checkUserRestaurant(Long userId) {
        CheckUserRestaurantDto result = restaurantRepository.checkUserRestaurant(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));


        return result;
    }

    @Override
    public RestaurantStatusDto restaurantOrderStatus(LocalDate date, Long userId) {

        User user = userAccessValidator.validateRestaurantUser(userId);
        CheckUserRestaurantDto result = checkUserRestaurant(user.getId());

        long totalOrderCount = 0L;
        double totalIncome = 0.0;

        TodayRestaurantStatusDto activeOrders = orderRepository.getTodayRestaurantStatus(result.getId(), date, List.of(OrderStatus.ACTIVE), Boolean.TRUE);
        TodayRestaurantStatusDto completedOrders = orderRepository.getTodayRestaurantStatus(result.getId(), date, List.of(OrderStatus.FINISHED), Boolean.FALSE);

        totalOrderCount = activeOrders.getTotalCount() + completedOrders.getTotalCount();
        totalIncome = activeOrders.getTotalPrice() + completedOrders.getTotalPrice();

        RestaurantStatusDto results = new RestaurantStatusDto();
        results.setActiveOrders(activeOrders.getTotalCount());
        results.setCompletedOrders(completedOrders.getTotalCount());
        results.setTotalOrders(totalOrderCount);
        results.setTotalIncome(totalIncome);

        return results;
    }

    @Override
    public ProgressOrderDto getProgressOrder(UUID orderId) {
        return orderRepository.getProgressOrder(orderId);
    }

    @Override
    public Boolean checkPayment(UUID orderId) {
        CheckOrderDto order = checkOrderExistance(orderId);

        if (!order.getOrderStatus().equals(OrderStatus.PAYMENT)) {
            throw new IllegalArgumentException("This order payment can not be checked as the order status is invalid");
        }

        return orderRepository.checkPayment(order.getId());
    }

    private CheckOrderDto checkOrderExistance(UUID orderId) {
        return orderRepository.checkOrderExistance(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public String finishOrder(UUID orderId, Long userId) {
        CheckOrderDto result = checkOrderExistance(orderId);

        checkUserRestaurant(userId);
        checkIfRestaurantExists(result.getRestaurantId());

        if (!result.getOrderStatus().equals(OrderStatus.ACTIVE)) {
            throw new IllegalArgumentException("This order can't be finished, the customer hasn't dined in yet");
        }

        orderRepository.finishOrder(result.getId());

        return "This order has been completed";
    }

    @Override
    public String markOrderReady(UUID orderId, Long userId) {
        CheckOrderDto result = checkOrderExistance(orderId);

        checkUserRestaurant(userId);
        checkIfRestaurantExists(result.getRestaurantId());

        if (!result.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("This order can't be marked as ready, it must be in CONFIRMED status");
        }

        orderRepository.markOrderReady(result.getId());

        return "Order has been marked as ready";
    }

    @Override
    public OrderDetailResponseDto getOrderByIdCustomer(UUID orderId) {
        return orderRepository.getOrderDetails(orderId);
    }

    @Override
    public List<IncomingOrderDto> getIncomingOrder(UUID restaurantId) {
        checkIfRestaurantExists(restaurantId);

        List<IncomingOrderRow> rows = orderRepository.getIncomingOrderRow(restaurantId);

        Map<UUID, IncomingOrderDto> orderMap = new LinkedHashMap<>();

        for (IncomingOrderRow r : rows) {
            orderMap.computeIfAbsent(r.getOrderId(), id -> {
                IncomingOrderDto dto = new IncomingOrderDto();
                dto.setRestaurantId(restaurantId);
                dto.setOrderId(id);
                dto.setMenuLists(new ArrayList<>());
                dto.setReceivedAt(r.getOrderTime());

                dto.setSubTotal(0.0);
                dto.setTotalPrice(0.0);
                return dto;
            });

            IncomingOrderDto dto = orderMap.get(r.getOrderId());

            MenuListDto menu = new MenuListDto();
            menu.setMenuCode(r.getMenuCode());
            menu.setMenuName(r.getMenuName());
            menu.setMenuPrice(r.getMenuPrice());
            menu.setMenuImageUrl(r.getMenuImageUrl());
            menu.setQuantity(r.getQuantity());
            menu.setTotalPrice(r.getMenuPrice() * r.getQuantity());

            dto.getMenuLists().add(menu);

            double newSubtotal = dto.getSubTotal() + menu.getTotalPrice();

            dto.setSubTotal(newSubtotal);
            dto.setTotalPrice(newSubtotal);
        }

        return new ArrayList<>(orderMap.values());
    }


    @Override
    public List<ConfirmedOrderDto> getConfirmedOrder(UUID restaurantId) {
        checkIfRestaurantExists(restaurantId);

        List<ConfirmedOrderRow> rows = orderRepository.getConfirmedOrderRow(restaurantId);

        Map<UUID, ConfirmedOrderDto> orderMap = new LinkedHashMap<>();

        for (ConfirmedOrderRow r : rows) {
            orderMap.computeIfAbsent(r.getOrderId(), id -> {
                ConfirmedOrderDto dto = new ConfirmedOrderDto();
                dto.setRestaurantId(restaurantId);
                dto.setOrderId(id);
                dto.setMenuLists(new ArrayList<>());
                dto.setPaymentImageUrl(r.getPaymentImageUrl());
                dto.setSubTotal(0.0);
                dto.setTotalPrice(0.0);
                return dto;
            });

            ConfirmedOrderDto dto = orderMap.get(r.getOrderId());

            MenuListDto menu = new MenuListDto();
            menu.setMenuCode(r.getMenuCode());
            menu.setMenuName(r.getMenuName());
            menu.setMenuPrice(r.getMenuPrice());
            menu.setMenuImageUrl(r.getMenuImageUrl());
            menu.setQuantity(r.getQuantity());
            menu.setTotalPrice(r.getMenuPrice() * r.getQuantity());

            dto.getMenuLists().add(menu);

            double newSubtotal = dto.getSubTotal() + menu.getTotalPrice();

            dto.setSubTotal(newSubtotal);
            dto.setTotalPrice(newSubtotal);
        }

        return new ArrayList<>(orderMap.values());
    }

    @Override
    public List<ActiveOrderDto> getActiveOrder(UUID restaurantId) {
        checkIfRestaurantExists(restaurantId);

        List<ActiveOrderRow> rows = orderRepository.getActiveOrderRows(restaurantId);

        Map<UUID, ActiveOrderDto> orderMap = new LinkedHashMap<>();

        for (ActiveOrderRow r : rows) {
            orderMap.computeIfAbsent(r.getOrderId(), id -> {
                ActiveOrderDto dto = new ActiveOrderDto();
                dto.setRestaurantId(restaurantId);
                dto.setOrderId(id);
                dto.setMenuLists(new ArrayList<>());
                dto.setDineInTime(r.getDineInTime());
                return dto;
            });

            ActiveOrderDto dto = orderMap.get(r.getOrderId());

            MenuListDto menu = new MenuListDto();
            menu.setMenuCode(r.getMenuCode());
            menu.setMenuName(r.getMenuName());
            menu.setMenuPrice(r.getMenuPrice());
            menu.setMenuImageUrl(r.getMenuImageUrl());
            menu.setQuantity(r.getQuantity());
            menu.setTotalPrice(r.getMenuPrice() * r.getQuantity());

            dto.getMenuLists().add(menu);
        }

        return new ArrayList<>(orderMap.values());
    }

    private PlaceOrderDto createOrder(AddOrderRequest request) {
        checkIfRestaurantExists(request.getRestaurantId());

        Order newRestaurantOrder = new Order();
        newRestaurantOrder.setRestaurantId(request.getRestaurantId());

        String orderMessage = request.getOrderMessage();
        newRestaurantOrder.setOrderMessage(
                (orderMessage == null || orderMessage.isBlank()) ? null : orderMessage
        );

        List<UUID> menuCodes = request.getItems()
                .stream()
                .map(OrderItemRequest::getMenuCode)
                .toList();

        Map<UUID, Menu> menuMap = menuRepository
                .getMenuDetail(menuCodes)
                .stream()
                .collect(Collectors.toMap(Menu::getMenuCode, Function.identity()));

        List<OrderItem> orderItems = new ArrayList<>();
        double subTotalPrice = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Menu menu = menuMap.get(itemRequest.getMenuCode());
            if (menu == null) {
                throw new NotFoundException("Menu not found: " + itemRequest.getMenuCode());
            }

            double itemTotalPrice = menu.getMenuPrice() * itemRequest.getQuantity();
            subTotalPrice += itemTotalPrice;

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuCode(itemRequest.getMenuCode());
            orderItem.setQuantity(itemRequest.getQuantity());

            orderItems.add(orderItem);
        }

        double totalPrice = subTotalPrice;

        newRestaurantOrder.setOrderTime(LocalDateTime.now());
        newRestaurantOrder.setIsActive(true);
        newRestaurantOrder.setSubTotal(subTotalPrice);
        newRestaurantOrder.setTotalPrice(totalPrice);
        newRestaurantOrder.setOrderStatus(OrderStatus.RECEIVED);
        newRestaurantOrder.setDineInTime(null);
        newRestaurantOrder.setCreatedDate(LocalDate.now());

        UUID savedOrder = orderRepository.addOrder(newRestaurantOrder);

        for (OrderItem item : orderItems) {
            item.setOrderId(savedOrder);
        }

        orderItemRepository.addMenuItems(orderItems);

        PlaceOrderDto result = new PlaceOrderDto();
        result.setOrderId(savedOrder);
        result.setCustomerId(request.getCustomerId());

        return result;
    }

    @Override
    public List<RecentOrderDto> getRecentOrderLists(UUID restaurantId) {
        return orderRepository.getRecentOrder(restaurantId);
    }

    @Override
    public List<OrderHistoryDto> getCustomerOrderHistory(String customerId, LocalDate startDate, LocalDate endDate, String status) {
        List<OrderHistoryRow> rows = orderRepository.getCustomerOrderHistory(customerId, startDate, endDate, status);
        return mapToOrderHistory(rows);
    }

    @Override
    public List<OrderHistoryDto> getRestaurantOrderHistory(LocalDate startDate, LocalDate endDate, String status, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);
        UUID restaurantId = restaurantRepository.getRestaurantId(user.getUserId());
        List<OrderHistoryRow> rows = orderRepository.getRestaurantOrderHistory(restaurantId, startDate, endDate, status);
        return mapToOrderHistory(rows);
    }

    @Override
    public PaymentModalDto getPaymentModalData(UUID orderId) {
        return orderRepository.getPaymentModalData(orderId);
    }

    private List<OrderHistoryDto> mapToOrderHistory(List<OrderHistoryRow> rows) {
        Map<UUID, OrderHistoryDto> orderMap = new LinkedHashMap<>();

        for (OrderHistoryRow row : rows) {
            orderMap.computeIfAbsent(row.getOrderId(), id -> {
                OrderHistoryDto dto = new OrderHistoryDto();
                dto.setOrderId(id);
                dto.setCreatedDate(row.getCreatedDate());
                dto.setRestaurantId(row.getRestaurantId());
                dto.setRestaurantName(row.getRestaurantName());
                dto.setCustomerName(row.getCustomerName());
                dto.setOrderTime(row.getOrderTime());
                dto.setOrderStatus(row.getOrderStatus());
                dto.setPaymentStatus(row.getPaymentStatus());
                dto.setSubTotal(row.getSubTotal());
                dto.setTotalPrice(row.getTotalPrice());
                dto.setMenuLists(new ArrayList<>());
                return dto;
            });

            OrderHistoryDto dto = orderMap.get(row.getOrderId());

            MenuListDto menuItem = new MenuListDto();
            menuItem.setMenuCode(row.getMenuCode());
            menuItem.setMenuName(row.getMenuName());
            menuItem.setMenuPrice(row.getMenuPrice());
            menuItem.setMenuImageUrl(row.getMenuImageUrl());
            menuItem.setQuantity(row.getQuantity());

            dto.getMenuLists().add(menuItem);
        }

        return new ArrayList<>(orderMap.values());
    }
}
