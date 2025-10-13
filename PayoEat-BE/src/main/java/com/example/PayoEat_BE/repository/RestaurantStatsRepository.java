package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantStatsRepository {
    private final JdbcClient jdbcClient;

    public TodayRestaurantStatsDto getTodayRestaurantStatus(UUID restaurantId, LocalDate date) {
        try {
            String sql = """
                    SELECT
                        COUNT(CASE WHEN o.order_status = 'CONFIRMED' THEN 1 END) AS active_orders,
                        COUNT(CASE WHEN o.order_status = 'FINISHED' THEN 1 END) AS completed_orders,
                        SUM(CASE WHEN o.order_status IN ('CONFIRMED', 'FINISHED') THEN o.total_price ELSE 0 END) AS income
                    FROM orders o
                    WHERE o.restaurant_id = :restaurantId AND o.created_date = :date
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurantId", restaurantId)
                    .param("date", date)
                    .query(TodayRestaurantStatsDto.class)
                    .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
