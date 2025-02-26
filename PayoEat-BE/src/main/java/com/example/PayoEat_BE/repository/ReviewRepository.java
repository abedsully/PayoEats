package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByRestaurantId(Long restaurantId);
    Optional<List<Review>> findByUserId(Long userId);
}
