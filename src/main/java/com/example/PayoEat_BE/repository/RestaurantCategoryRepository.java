package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.RestaurantCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RestaurantCategoryRepository {
    private final JdbcClient jdbcClient;

    public Integer add(RestaurantCategory req) {
        String sql = """
                insert into restaurant_category (category_name, added_at, is_active) values (:category_name, added_at, is_active);
                """;

        return jdbcClient.sql(sql)
                .param("category_name" ,req.getCategoryName())
                .param("added_at", req.getAddedAt())
                .param("is_active", Boolean.TRUE)
                .update();
    }

    public List<RestaurantCategory> findByIsActiveTrue() {
        String sql = "SELECT * from restaurant_category where is_active = true";

        return jdbcClient.sql(sql)
                .query(RestaurantCategory.class)
                .list();
    }

    public Optional<RestaurantCategory> findByIdAndIsActiveTrue(Long id) {
        String sql = "SELECT * from restaurant_category where is_active = true and id = :id";

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(RestaurantCategory.class)
                .optional();
    }
}
