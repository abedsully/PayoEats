package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    boolean existsByNameAndIsActiveTrue(String name);
    Optional <Restaurant> findByIdAndIsActiveTrue(UUID id);
    Optional <Restaurant> findByIdAndIsActiveFalse(UUID id);
    List<Restaurant> findByIsActiveTrue();
    boolean existsByUserIdAndIsActiveTrue(Long userId);
    List<Restaurant> findByRestaurantCategoryAndIsActiveTrueAndIdNot(Long restaurantCategory, UUID id);
}
