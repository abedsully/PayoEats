package com.example.PayoEat_BE.service.orders;

import com.example.PayoEat_BE.dto.*;
import com.example.PayoEat_BE.dto.orders.OrderDetailResponseDto;
import com.example.PayoEat_BE.dto.orders.OrderHistoryDto;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.OrderItem;
import com.example.PayoEat_BE.request.order.AddOrderRequest;
import com.example.PayoEat_BE.request.order.CancelOrderRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    String generateOrderIdQrCode(UUID orderId);
    PlaceOrderDto addOrder(AddOrderRequest request);
    void addPaymentProof(UUID orderId, MultipartFile file);
    void confirmOrder(UUID orderId, Long userId);
    void confirmOrderPayment(UUID orderId, Long userId);
    void rejectOrderPayment(RejectOrderPaymentDto dto, Long userId);
    String finishOrder(UUID orderId, Long userId);
    OrderDetailResponseDto getOrderByIdCustomer(UUID orderId);
    List<IncomingOrderDto> getIncomingOrder(UUID restaurantId);
    List<ConfirmedOrderDto> getConfirmedOrder(UUID restaurantId);
    List<ActiveOrderDto> getActiveOrder(UUID restaurantId);
    String cancelOrderByRestaurant(CancelOrderRequest request, Long userId);
    String cancelOrderByCustomer(CancelOrderRequest request);
    String processOrderToActive(UUID orderId);
    RestaurantStatusDto restaurantOrderStatus(LocalDate date, Long userId);
    ProgressOrderDto getProgressOrder(UUID orderId);
    Boolean checkPayment(UUID orderId);
    List<RecentOrderDto> getRecentOrderLists(UUID restaurantId);
    List<OrderHistoryDto> getCustomerOrderHistory(String customerId, LocalDate startDate, LocalDate endDate, String status);
    List<OrderHistoryDto> getRestaurantOrderHistory(UUID restaurantId, LocalDate startDate, LocalDate endDate, String status);
    PaymentModalDto getPaymentModalData(UUID orderId);
}
