package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.ReviewRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    public List<Review> getReviewsByRestaurantId(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaruant not found with id: " + restaurantId));

        return reviewRepository.findByRestaurantId(restaurant.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        return reviewRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Review not found with user id: " + userId));
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

        // tambah is_active nanti
        if (!user.getRoles().equals(UserRoles.CUSTOMER)) {
            throw new ForbiddenException("You can't review a restaurant, if you're not a customer");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        if(restaurant.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Sorry, you can't review your own restaurant");
        }

        if (request.getReviewContent().isEmpty()) {
            throw new InvalidException("Please enter the review");
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

