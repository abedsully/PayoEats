package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Review;
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
    public void addReview(AddReviewRequest request, Long userId) {
        createReview(request, userId);
    }

    @Override
    public List<ReviewDto> getReviewsByRestaurantId(UUID restaurantId) {
        List<ReviewDto> reviewDtoList = reviewRepository.findReviewsByRestaurantId(restaurantId);

        if (reviewDtoList.isEmpty()) {
            throw new NotFoundException("No reviews found for restaurant id: " + restaurantId);
        }

        return reviewDtoList;
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {

        return reviewRepository.findByUserId(userId);
    }

    @Override
    public ReviewDto convertToDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public List<ReviewDto> getConvertedReviews(List<Review> reviewList) {
        return reviewList.stream().map(this::convertToDto).toList();
    }

    private void createReview(AddReviewRequest request, Long userId) {
        Restaurant restaurant = restaurantRepository.getDetail(request.getRestaurantId(), Boolean.TRUE)
                .orElseThrow(() -> new NotFoundException("as"));
        if (request.getReviewContent().isEmpty()) {
            throw new InvalidException("Please enter the review");
        }

        if (request.getRating() < 1) {
            throw new InvalidException("Rating can not be lower than 1 star");
        }

        Double currentRestaurantRating = restaurant.getRating();
        Long totalRating = restaurant.getTotalRatingCount() + 1;


        Double ratingRestaurant = (currentRestaurantRating + request.getRating() / totalRating);

        Review review = new Review();
        review.setReviewContent(request.getReviewContent());
        review.setUserId(userId);
        review.setRestaurantId(restaurant.getId());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(null);
        review.setIsActive(true);
        review.setRating(request.getRating());

        reviewRepository.insertReview(review, userId);
        restaurantRepository.addReview(restaurant.getId(), ratingRestaurant, totalRating);
    }
}

