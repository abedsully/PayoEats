package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ReviewRepository {
    private final JdbcClient jdbcClient;

    public Integer insertReview(Review review, Long userId) {
        try {
            String sql = """
                            INSERT INTO review (
                                review_content, user_id, created_at, updated_at, 
                                is_active, restaurant_id, rating, review_image_url
                            ) VALUES (
                                :reviewContent, :userId, :createdAt, :updatedAt,
                                :isActive, :restaurantId, :rating, :reviewImageUrl
                            )
                        """;

            return jdbcClient.sql(sql)
                    .param("reviewContent", review.getReviewContent())
                    .param("userId", userId)
                    .param("createdAt", LocalDateTime.now())
                    .param("updatedAt", null)
                    .param("isActive", true)
                    .param("restaurantId", review.getRestaurantId())
                    .param("rating", review.getRating())
                    .param("reviewImageUrl", review.getReviewImageUrl())
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Review> findByUserId(Long userId) {
        try {
            String sql = """
                    select * from review where user_id = :user_id;
                    """;

            return jdbcClient.sql(sql)
                    .param("user_id", userId)
                    .query(Review.class)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ReviewDto> findReviewsByRestaurantId(UUID restaurantId) {
        String sql = """
            SELECT r.review_content, r.created_at, r.restaurant_id, r.user_id,
                   r.rating, r.review_image_url, u.username
            FROM review r
            JOIN users u ON r.user_id = u.id
            WHERE r.restaurant_id = :restaurantId
              AND r.is_active = true
            ORDER BY r.created_at DESC
        """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .query(this::mapRowToReviewDto)
                .list();
    }

    private ReviewDto mapRowToReviewDto(ResultSet rs, int rowNum) throws SQLException {
        ReviewDto dto = new ReviewDto();
        dto.setReviewContent(rs.getString("review_content"));
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dto.setRestaurantId(UUID.fromString(rs.getString("restaurant_id")));
        dto.setUserId(rs.getLong("user_id"));
        dto.setUsername(rs.getString("username"));
        dto.setRating(rs.getDouble("rating"));
        dto.setReviewImageUrl(rs.getString("review_image_url"));
        return dto;
    }

}
