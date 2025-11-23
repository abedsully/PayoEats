package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {
    private final JdbcClient jdbcClient;

    public int addMenuItems(List<OrderItem> items) {
        try {
            String sql = """
            INSERT INTO order_items (
                menu_code,
                quantity,
                order_id
            ) VALUES (
                :menu_code,
                :quantity,
                :order_id
            )
        """;

            int totalInserted = 0;

            for (OrderItem item : items) {
                int inserted = jdbcClient.sql(sql)
                        .param("menu_code", item.getMenuCode())
                        .param("quantity", item.getQuantity())
                        .param("order_id", item.getOrderId())
                        .update();

                totalInserted += inserted;
            }

            return totalInserted;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
