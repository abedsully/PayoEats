package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.ProgressOrderDto;

import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import com.example.PayoEat_BE.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryy {
    private final JdbcClient jdbcClient;

    public ProgressOrderDto getProgressOrder(UUID orderId) {
        try {
            String sql = "SELECT o.id AS orderId, r.name AS restaurantName, o.total_price AS totalPrice, o.order_status AS orderStatus, " +
                    "o.payment_status as paymentStatus, o.payment_image_rejection_reason AS additionalInfo " +
                    "FROM orders o join restaurant r on o.restaurant_id  = r.id " +
                    "WHERE o.id = :orderId";

            return jdbcClient.sql(sql)
                    .param("orderId", orderId)
                    .query(ProgressOrderDto.class).single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Order> getActiveOrders(UUID restaurantId) {
        try {
            String sql = """
                    SELECT * from orders o
                    WHERE restaurant_id = :restaurant_id AND created_date = :created_date AND is_active = TRUE AND order_status in ('DINING', 'CONFIRMED')
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("created_date", LocalDate.now())
                    .query(Order.class)
                    .stream().toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UUID> findExpiredOrders(LocalDateTime cutOffTime) {
        try {
            String sql = """
            SELECT id FROM orders o 
            WHERE DATE(o.created_date) = :date 
              AND o.payment_begin_at < :cutOffTime
              AND o.payment_image IS NULL 
              AND o.order_status = :status 
              AND o.is_active = TRUE
              AND o.payment_status = :payment_status
        """;

            return jdbcClient.sql(sql)
                    .param("date", LocalDate.now())
                    .param("cutOffTime", cutOffTime)
                    .param("status", OrderStatus.PAYMENT.name())
                    .param("payment_status", PaymentStatus.PENDING.name())
                    .query(UUID.class)
                    .stream()
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch expired orders", e);
        }
    }


    public void updateOrderStatus(UUID orderId) {
        try {
            String sql = """
            UPDATE orders 
            SET order_status = :order_status,
                cancellation_reason = :reason,
                payment_status = :payment_status
                is_active = FALSE
            WHERE id = :order_id AND is_active = TRUE
        """;

            jdbcClient.sql(sql)
                    .param("order_status", OrderStatus.CANCELLED.name())
                    .param("payment_status", PaymentStatus.EXPIRED.name())
                    .param("reason", "Payment time expired")
                    .param("order_id", orderId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    public boolean checkPayment(UUID orderId) {
        try {
             String sql = """
                     SELECT EXISTS (select 1 FROM orders WHERE id = :order_id and payment_image is not null and order_status = 'CONFIRMED')
                     """;

             return jdbcClient.sql(sql)
                     .param("order_id", orderId)
                     .query(Boolean.class)
                     .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
