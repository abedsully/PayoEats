package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    boolean existsByCategoryNameAndIsActiveTrue(String categoryName);
    List<RestaurantCategory> findByIsActiveTrue();
    List<RestaurantCategory> findByCategoryNameContainingIgnoreCase(String categoryName);
}
