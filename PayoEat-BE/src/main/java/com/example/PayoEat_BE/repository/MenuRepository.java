package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByRestaurantId(UUID restaurantId);
    Optional<Menu> findByMenuCodeAndRestaurantId(UUID menuCode, UUID restaurantId);
    Optional<Menu> findByMenuCodeAndIsActiveTrue(UUID menuCode);
    List<Menu> findByRestaurantIdAndMenuNameContainingIgnoreCaseAndIsActiveTrue(UUID restaurantId, String menuName);
}
