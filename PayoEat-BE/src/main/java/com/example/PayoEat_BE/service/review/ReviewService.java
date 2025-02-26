package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.exceptions.AlreadyExistException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.ReviewRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public Review addReview(AddReviewRequest request) {
        return reviewRepository.save(createReview(request));
    }

    @Override
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public ReviewDto convertToDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public List<ReviewDto> getConvertedReviews(List<Review> reviewList) {
        return reviewList.stream().map(this::convertToDto).toList();
    }

    private Review createReview(AddReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId()));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        if(restaurant.getUserId().equals(user.getId())) {
            throw new InvalidException("Sorry, you can't review your own restaurant");
        }

        Review review = new Review();
        review.setReviewContent(request.getReviewContent());
        review.setUserId(user.getId());
        review.setRestaurantId(restaurant.getId());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(null);
        review.setIsActive(true);

        return review;
    }
}

