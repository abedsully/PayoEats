package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.RestaurantReviewStatsDto;
import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.dto.dashboard.RatingBreakdownDto;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ReviewRepository {
    private final JdbcClient jdbcClient;

    public Integer addReview(AddReviewRequest review, String imageUrl, String customerName) {
        try {
            String sql = """
                            INSERT INTO review (
                                review_content, customer_name, created_at, updated_at,
                                is_active, restaurant_id, rating, review_image_url, order_id, customer_id
                            ) VALUES (
                                :reviewContent, :customerName, :createdAt, :updatedAt,
                                :isActive, :restaurantId, :rating, :reviewImageUrl, :orderId, :customerId
                            )
                        """;

            return jdbcClient.sql(sql)
                    .param("reviewContent", review.getReviewContent())
                    .param("customerName", customerName)
                    .param("createdAt", LocalDateTime.now())
                    .param("updatedAt", null)
                    .param("isActive", true)
                    .param("restaurantId", review.getRestaurantId())
                    .param("rating", review.getRating())
                    .param("reviewImageUrl", imageUrl)
                    .param("orderId", review.getOrderId())
                    .param("customerId", review.getCustomerId())
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
            select
                r.review_content,
                r.created_at,
                r.restaurant_id,
                r.customer_name,
                r.rating,
                r.review_image_url,
                string_agg(CONCAT(oi.quantity, 'x ', m.menu_name), ', ') as review_order_lists
            from
                review r
            left join order_items oi on
                oi.order_id = r.order_id
            left join menu m on
                m.menu_code = oi.menu_code
            where
                r.restaurant_id = :restaurantId
                and r.is_active = true
            group by
                r.review_content,
                r.created_at,
                r.restaurant_id,
                r.customer_name,
                r.rating,
                r.review_image_url
            order by
                r.created_at desc
        """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .query(this::mapRowToReviewDto)
                .list();
    }

    public RestaurantReviewStatsDto getRestaurantReviewStats(UUID restaurantId) {
        try {
            String sql = """
                SELECT name AS restaurantName, rating, total_rating AS totalRating,
                       color, restaurant_image_url AS restaurantImageUrl
                FROM restaurant
                WHERE id = :id AND is_active = true
                """;

            RestaurantReviewStatsDto stats = jdbcClient.sql(sql)
                    .param("id", restaurantId)
                    .query(RestaurantReviewStatsDto.class)
                    .single();

            List<RatingBreakdownDto> breakdown = getRatingBreakdown(restaurantId);

            Map<Integer, RatingBreakdownDto> map = breakdown.stream()
                    .collect(Collectors.toMap(RatingBreakdownDto::getRating, b -> b));

            List<RatingBreakdownDto> full = new ArrayList<>();

            for (int i = 5; i >= 1; i--) {
                RatingBreakdownDto dto = map.getOrDefault(i, new RatingBreakdownDto());
                dto.setRating(i);
                dto.setTotal(dto.getTotal() == null ? 0L : dto.getTotal());
                full.add(dto);
            }

            stats.setRatingBreakdown(full);

            return stats;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    public List<RatingBreakdownDto> getRatingBreakdown(UUID restaurantId) {
        String sql = """
        SELECT rating, COUNT(*) AS total
        FROM review
        WHERE is_active = true AND restaurant_id = :restaurantId
        GROUP BY rating
        ORDER BY rating DESC
    """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .query((rs, row) -> {
                    RatingBreakdownDto dto = new RatingBreakdownDto();
                    dto.setRating(rs.getInt("rating"));
                    dto.setTotal(rs.getLong("total"));
                    return dto;
                })
                .list();
    }



    private ReviewDto mapRowToReviewDto(ResultSet rs, int rowNum) throws SQLException {
        ReviewDto dto = new ReviewDto();
        dto.setReviewContent(rs.getString("review_content"));
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dto.setRestaurantId(UUID.fromString(rs.getString("restaurant_id")));
        dto.setCustomerName(rs.getString("customer_name"));
        dto.setRating(rs.getDouble("rating"));
        dto.setReviewImageUrl(rs.getString("review_image_url"));
        dto.setReviewOrderLists(rs.getString("review_order_lists"));
        return dto;
    }

}
