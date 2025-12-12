package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.RestaurantReviewStatsDto;
import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IReviewService {
    void addReview(AddReviewRequest request, MultipartFile file);
    List<ReviewDto> getReviewsByRestaurantId(UUID restaurantId);
    List<Review> getReviewsByUserId(Long userId);
    ReviewDto convertToDto(Review review);
    List<ReviewDto> getConvertedReviews (List<Review> reviewList);
    RestaurantReviewStatsDto getRestaurantReviewStats(UUID restaurantId);
}
