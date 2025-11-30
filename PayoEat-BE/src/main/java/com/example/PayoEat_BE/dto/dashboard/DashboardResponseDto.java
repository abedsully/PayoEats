package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashboardResponseDto {
    private DashboardStatsDto stats;
    private List<DailyStatsDto> weeklyData;
    private List<PopularItemDto> popularItems;
    private LocalDateTime lastUpdated;
}
