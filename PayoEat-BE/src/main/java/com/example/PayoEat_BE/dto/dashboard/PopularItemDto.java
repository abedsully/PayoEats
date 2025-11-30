package com.example.PayoEat_BE.dto.dashboard;

import lombok.Data;
import java.util.UUID;

@Data
public class PopularItemDto {
    private UUID menuCode;
    private String menuName;
    private String menuImage;
    private Long totalOrdered;
    private Double revenue;
}
