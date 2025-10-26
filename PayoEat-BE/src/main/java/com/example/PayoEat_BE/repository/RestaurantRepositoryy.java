package com.example.PayoEat_BE.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryy {
    private final JdbcClient jdbcClient;

    public UUID getRestaurantId(Long userId) {
        try {
            String sql = "SELECT id from restaurant where user_id = :user_id";

            return jdbcClient.sql(sql)
                    .param("user_id", userId)
                    .query(UUID.class)
                    .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
