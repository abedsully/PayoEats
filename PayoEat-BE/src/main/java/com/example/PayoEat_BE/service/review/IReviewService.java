package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.request.review.AddReviewRequest;

import java.util.List;
import java.util.UUID;

public interface IReviewService {
    Review addReview(AddReviewRequest request);
    List<Review> getReviewsByRestaurantId(UUID restaurantId);
    List<Review> getReviewsByUserId(Long userId);
    ReviewDto convertToDto(Review review);
    List<ReviewDto> getConvertedReviews (List<Review> reviewList);
}
