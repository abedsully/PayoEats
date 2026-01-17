package com.example.PayoEat_BE.dto.orders;

import lombok.Data;
import java.util.UUID;

@Data
public class OrderItemDetailDto {
    private UUID orderItemId;
    private UUID menuCode;
    private Long quantity;
    private String menuName;
    private Double menuPrice;
    private String menuImageUrl;
}
