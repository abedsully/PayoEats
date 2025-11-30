package com.example.PayoEat_BE.service.restaurant_stats;

import com.example.PayoEat_BE.dto.TodayRestaurantStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DashboardResponseDto;
import com.example.PayoEat_BE.dto.dashboard.DashboardStatsDto;
import com.example.PayoEat_BE.dto.dashboard.DailyStatsDto;
import com.example.PayoEat_BE.dto.dashboard.PopularItemDto;
import com.example.PayoEat_BE.dto.dashboard.WeeklyStatsDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantStatsRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantStatsService implements IRestaurantStatsService {
    private final UserRepository userRepository;
    private final RestaurantStatsRepository restaurantStatsRepository;

    @Override
    public TodayRestaurantStatsDto getTodayRestaurantStats(UUID restaurantId, Long userId) {
        return restaurantStatsRepository.getTodayRestaurantStatus(restaurantId, LocalDate.now());
    }

    @Override
    public DashboardResponseDto getCompleteDashboard(UUID restaurantId, Long userId,
                                                      LocalDate startDate, LocalDate endDate, Integer days) {
        // If no date range provided, use defaults
        LocalDate effectiveEndDate;
        LocalDate effectiveStartDate;

        if (startDate == null && endDate == null) {
            effectiveEndDate = LocalDate.now();
            effectiveStartDate = effectiveEndDate.minusDays(days != null ? days - 1 : 6);
        } else if (startDate != null && endDate != null) {
            effectiveStartDate = startDate;
            effectiveEndDate = endDate;
        } else if (endDate != null) {
            effectiveEndDate = endDate;
            effectiveStartDate = effectiveEndDate.minusDays(days != null ? days - 1 : 6);
        } else {
            effectiveStartDate = startDate;
            effectiveEndDate = startDate.plusDays(days != null ? days - 1 : 6);
        }

        // Calculate today and yesterday for comparison (using effective end date as "today")
        LocalDate today = effectiveEndDate;
        LocalDate yesterday = today.minusDays(1);

        // Calculate week boundaries (Monday to Sunday)
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today;
        LocalDate lastWeekStart = weekStart.minusWeeks(1);
        LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

        // Get all data
        DashboardStatsDto stats = restaurantStatsRepository.getDashboardStats(restaurantId, today, yesterday);
        WeeklyStatsDto weeklyStats = restaurantStatsRepository.getWeeklyStats(
                restaurantId, weekStart, weekEnd, lastWeekStart, lastWeekEnd
        );

        // Merge weekly stats into main stats
        stats.setWeekRevenue(weeklyStats.getWeekRevenue());
        stats.setLastWeekRevenue(weeklyStats.getLastWeekRevenue());
        stats.setWeekOrders(weeklyStats.getWeekOrders());
        stats.setLastWeekOrders(weeklyStats.getLastWeekOrders());

        // Calculate percentage changes
        stats.setRevenueChangePercent(calculatePercentChange(stats.getTodayRevenue(), stats.getYesterdayRevenue()));
        stats.setOrdersChangePercent(calculatePercentChange(stats.getTodayOrders(), stats.getYesterdayOrders()));
        stats.setWeekRevenueChangePercent(calculatePercentChange(stats.getWeekRevenue(), stats.getLastWeekRevenue()));
        stats.setWeekOrdersChangePercent(calculatePercentChange(stats.getWeekOrders(), stats.getLastWeekOrders()));

        // Get daily breakdown for chart using custom date range
        List<DailyStatsDto> weeklyData = restaurantStatsRepository.getDailyBreakdown(
                restaurantId, effectiveStartDate, effectiveEndDate
        );

        // Add day names
        weeklyData.forEach(day -> {
            day.setDayName(day.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        });

        // Get popular items using custom date range
        List<PopularItemDto> popularItems = restaurantStatsRepository.getPopularItems(
                restaurantId, effectiveStartDate, effectiveEndDate, 6
        );

        // Build response
        DashboardResponseDto response = new DashboardResponseDto();
        response.setStats(stats);
        response.setWeeklyData(weeklyData);
        response.setPopularItems(popularItems);
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    private Double calculatePercentChange(Number current, Number previous) {
        if (previous == null || previous.doubleValue() == 0) return 0.0;
        return ((current.doubleValue() - previous.doubleValue()) / previous.doubleValue()) * 100;
    }
}
