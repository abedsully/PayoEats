package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.RestaurantApproval;
import com.example.PayoEat_BE.request.restaurant.ReviewRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantApprovalRepository {
    private final JdbcClient jdbcClient;

    public UUID addRestaurantApproval(RestaurantApproval request) {
        try {
            UUID approvalId = UUID.randomUUID();


            String sql = """
                    INSERT INTO restaurant_approval (
                        id,
                        restaurant_id,
                        restaurant_name,
                        restaurant_image_url,
                        user_id,
                        reason,
                        requested_at,
                        processed_at,
                        is_approved,
                        is_active
                    ) VALUES (
                        :id,
                        :restaurant_id,
                        :restaurant_name,
                        :restaurant_image_url,
                        :user_id,
                        :reason,
                        :requested_at,
                        :processed_at,
                        :is_approved,
                        :is_active
                    )
                    """;

            jdbcClient.sql(sql)
                    .param("id", approvalId)
                    .param("restaurant_id", request.getRestaurantId())
                    .param("restaurant_name", request.getRestaurantName())
                    .param("restaurant_image_url", request.getRestaurantImageUrl())
                    .param("user_id", request.getUserId())
                    .param("requested_at", LocalDateTime.now())
                    .param("reason", null)
                    .param("processed_at", null)
                    .param("is_approved", Boolean.FALSE)
                    .param("is_active", Boolean.TRUE)
                    .update();

            return approvalId;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<RestaurantApproval> findById(UUID id) {
        try {
            String sql = """
                    select * from restaurant_approval where id = :id;
                    """;

            return jdbcClient.sql(sql)
                    .param("id", id)
                    .query(RestaurantApproval.class)
                    .optional();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<RestaurantApproval> getAllRestaurantApproval() {
        try {
            String sql = """
                    select * from restaurant_approval;
                    """;

            return jdbcClient.sql(sql)
                    .query(RestaurantApproval.class)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
