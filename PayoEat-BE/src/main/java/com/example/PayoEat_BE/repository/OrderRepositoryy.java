package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.ProgressOrderDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
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
}
