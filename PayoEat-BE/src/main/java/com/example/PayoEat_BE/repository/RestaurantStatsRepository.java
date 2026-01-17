package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DashboardStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DailyStatsDto;
import com.example.PayoEat_BE.dto.dashboard.PopularItemDto;
import com.example.PayoEat_BE.dto.dashboard.WeeklyStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantStatsRepository {
    private final JdbcClient jdbcClient;

    public TodayRestaurantStatsDto getTodayRestaurantStatus(UUID restaurantId, LocalDate date) {
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
    }

    public DashboardStatsDto getDashboardStats(UUID restaurantId, LocalDate today, LocalDate yesterday) {
        String sql = """
                WITH today_stats AS (
                    SELECT
                        COALESCE(COUNT(*), 0) as total_orders,
                        COALESCE(COUNT(CASE WHEN order_status = 'FINISHED' THEN 1 END), 0) as completed_orders,
                        COALESCE(COUNT(CASE WHEN order_status = 'CANCELLED' THEN 1 END), 0) as cancelled_orders,
                        COALESCE(SUM(CASE WHEN order_status IN ('CONFIRMED', 'FINISHED') THEN total_price ELSE 0 END), 0) as revenue
                    FROM orders
                    WHERE restaurant_id = :restaurantId
                    AND created_date = :today
                ),
                yesterday_stats AS (
                    SELECT
                        COALESCE(COUNT(*), 0) as total_orders,
                        COALESCE(COUNT(CASE WHEN order_status = 'FINISHED' THEN 1 END), 0) as completed_orders,
                        COALESCE(COUNT(CASE WHEN order_status = 'CANCELLED' THEN 1 END), 0) as cancelled_orders,
                        COALESCE(SUM(CASE WHEN order_status IN ('CONFIRMED', 'FINISHED') THEN total_price ELSE 0 END), 0) as revenue
                    FROM orders
                    WHERE restaurant_id = :restaurantId
                    AND created_date = :yesterday
                )
                SELECT
                    COALESCE(t.total_orders, 0) as todayOrders,
                    COALESCE(t.completed_orders, 0) as todayCompleted,
                    COALESCE(t.cancelled_orders, 0) as todayCancelled,
                    COALESCE(t.revenue, 0.0) as todayRevenue,
                    COALESCE(y.total_orders, 0) as yesterdayOrders,
                    COALESCE(y.completed_orders, 0) as yesterdayCompleted,
                    COALESCE(y.cancelled_orders, 0) as yesterdayCancelled,
                    COALESCE(y.revenue, 0.0) as yesterdayRevenue,
                    CASE WHEN t.total_orders > 0 THEN t.revenue::DOUBLE PRECISION / t.total_orders ELSE 0.0 END as avgOrderValue,
                    CASE WHEN t.total_orders > 0 THEN t.completed_orders::DOUBLE PRECISION / t.total_orders * 100 ELSE 0.0 END as completionRate
                FROM today_stats t, yesterday_stats y
                """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .param("today", today)
                .param("yesterday", yesterday)
                .query(DashboardStatsDto.class)
                .optional()
                .orElse(new DashboardStatsDto());
    }

    public WeeklyStatsDto getWeeklyStats(UUID restaurantId, LocalDate weekStart, LocalDate weekEnd,
                                          LocalDate lastWeekStart, LocalDate lastWeekEnd) {
        String sql = """
                WITH this_week AS (
                    SELECT
                        COALESCE(COUNT(*), 0) as total_orders,
                        COALESCE(SUM(CASE WHEN order_status IN ('CONFIRMED', 'FINISHED') THEN total_price ELSE 0 END), 0) as revenue
                    FROM orders
                    WHERE restaurant_id = :restaurantId
                    AND created_date BETWEEN :weekStart AND :weekEnd
                ),
                last_week AS (
                    SELECT
                        COALESCE(COUNT(*), 0) as total_orders,
                        COALESCE(SUM(CASE WHEN order_status IN ('CONFIRMED', 'FINISHED') THEN total_price ELSE 0 END), 0) as revenue
                    FROM orders
                    WHERE restaurant_id = :restaurantId
                    AND created_date BETWEEN :lastWeekStart AND :lastWeekEnd
                )
                SELECT
                    COALESCE(tw.total_orders, 0) as weekOrders,
                    COALESCE(tw.revenue, 0.0) as weekRevenue,
                    COALESCE(lw.total_orders, 0) as lastWeekOrders,
                    COALESCE(lw.revenue, 0.0) as lastWeekRevenue
                FROM this_week tw, last_week lw
                """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .param("weekStart", weekStart)
                .param("weekEnd", weekEnd)
                .param("lastWeekStart", lastWeekStart)
                .param("lastWeekEnd", lastWeekEnd)
                .query(WeeklyStatsDto.class)
                .optional()
                .orElse(new WeeklyStatsDto());
    }

    public List<DailyStatsDto> getDailyBreakdown(UUID restaurantId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    created_date as date,
                    COALESCE(COUNT(*), 0) as orders,
                    COALESCE(COUNT(CASE WHEN order_status = 'FINISHED' THEN 1 END), 0) as completed,
                    COALESCE(COUNT(CASE WHEN order_status = 'CANCELLED' THEN 1 END), 0) as cancelled,
                    COALESCE(SUM(CASE WHEN order_status IN ('CONFIRMED', 'FINISHED') THEN total_price ELSE 0 END), 0) as revenue
                FROM orders
                WHERE restaurant_id = :restaurantId
                AND created_date BETWEEN :startDate AND :endDate
                GROUP BY created_date
                ORDER BY created_date
                """;

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .query(DailyStatsDto.class)
                .list();
    }

    public List<PopularItemDto> getPopularItems(UUID restaurantId, LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                SELECT
                    m.menu_code as menuCode,
                    m.menu_name as menuName,
                    m.menu_image_url as menuImage,
                    COUNT(oi.menu_code) as totalOrdered,
                    COALESCE(SUM(m.menu_price * oi.quantity), 0.0)::DOUBLE PRECISION as revenue
                FROM order_items oi
                JOIN orders o ON oi.order_id = o.id
                JOIN menu m ON oi.menu_code = m.menu_code
                WHERE o.restaurant_id = :restaurantId
                AND o.created_date BETWEEN :startDate AND :endDate
                AND o.order_status IN ('CONFIRMED', 'FINISHED')
                GROUP BY m.menu_code, m.menu_name, m.menu_image_url
                ORDER BY COUNT(oi.menu_code) DESC, SUM(m.menu_price * oi.quantity) DESC
                LIMIT %d
                """.formatted(limit);

        return jdbcClient.sql(sql)
                .param("restaurantId", restaurantId)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .query(PopularItemDto.class)
                .list();
    }
}
