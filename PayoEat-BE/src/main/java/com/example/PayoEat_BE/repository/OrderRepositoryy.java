package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.ProgressOrderDto;

import com.example.PayoEat_BE.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryy {
    private final JdbcClient jdbcClient;

    public ProgressOrderDto getProgressOrder(UUID orderId) {
        try {
            String sql = "SELECT o.id AS orderId, r.name AS restaurantName, o.total_price AS totalPrice, o.order_status AS orderStatus " +
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
}
