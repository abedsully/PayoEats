package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByRestaurantId(Long restaurantId);
    Optional<Menu> findByMenuCodeAndIsActiveTrue(String menuCode);
}
