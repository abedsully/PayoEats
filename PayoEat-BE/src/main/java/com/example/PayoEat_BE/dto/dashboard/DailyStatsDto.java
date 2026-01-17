package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyStatsDto {
    private LocalDate date;
    private String dayName;
    private Long orders;
    private Double revenue;
    private Long completed;
    private Long cancelled;
}
