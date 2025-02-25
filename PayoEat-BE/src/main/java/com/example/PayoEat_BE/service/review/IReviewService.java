package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.request.review.AddReviewRequest;

import java.util.List;

public interface IReviewService {
    Review addReview(AddReviewRequest request);
    List<Review> getReviewsByRestaurantId(Long restaurantId);
    ReviewDto convertToDto(Review review);
}
