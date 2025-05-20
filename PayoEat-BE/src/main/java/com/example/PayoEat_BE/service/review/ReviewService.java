package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.ReviewRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import com.example.PayoEat_BE.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;


    @Override
    public Review addReview(AddReviewRequest request, MultipartFile reviewImage, Long userId) {
        return reviewRepository.save(createReview(request, reviewImage, userId));
    }

    @Override
    public List<ReviewDto> getReviewsByRestaurantId(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaruant not found with id: " + restaurantId));

        List<Review> reviewList = reviewRepository.findByRestaurantId(restaurant.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            Long userId = review.getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found with id " + userId));

            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRating(review.getRating());
            reviewDto.setReviewContent(review.getReviewContent());
            reviewDto.setUsername(user.getUsername());
            reviewDto.setRestaurantId(review.getRestaurantId());
            reviewDto.setCreatedAt(review.getCreatedAt());
            reviewDto.setUserId(user.getId());
            reviewDto.setImageId(review.getImageId());

            reviewDtoList.add(reviewDto);
        }

        return reviewDtoList;
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

    private Review createReview(AddReviewRequest request, MultipartFile reviewImage, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

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

        if (request.getRating() < 1) {
            throw new InvalidException("Rating can not be lower than 1 star");
        }

        Double currentRestaurantRating = restaurant.getRating();
        Long totalRating = restaurant.getTotalRating() + 1;

        restaurant.setRating((currentRestaurantRating + request.getRating()) / totalRating);
        restaurant.setTotalRating(totalRating);

        Review review = new Review();
        review.setReviewContent(request.getReviewContent());
        review.setUserId(user.getId());
        review.setRestaurantId(restaurant.getId());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(null);
        review.setIsActive(true);
        review.setRating(request.getRating());

        Image imageReview = imageService.saveReviewImage(reviewImage, review.getId());
        imageReview.setReviewId(review.getId());
        review.setImageId(imageReview.getId());

        restaurantRepository.save(restaurant);

        return review;
    }
}

