package com.example.PayoEat_BE.service.review;

import com.example.PayoEat_BE.dto.RestaurantReviewStatsDto;
import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.dto.orders.OrderDetailResponseDto;
import com.example.PayoEat_BE.enums.UploadType;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.ReviewRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import com.example.PayoEat_BE.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UploadService uploadService;
    private final OrderRepository orderRepository;


    @Override
    public void addReview(AddReviewRequest request, MultipartFile file) {
        createReview(request, file);
    }

    @Override
    public List<ReviewDto> getReviewsByRestaurantId(UUID restaurantId) {
        return reviewRepository.findReviewsByRestaurantId(restaurantId);
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

    @Override
    public RestaurantReviewStatsDto getRestaurantReviewStats(UUID restaurantId) {
        return reviewRepository.getRestaurantReviewStats(restaurantId);
    }


    private void createReview(AddReviewRequest request, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.getDetail(request.getRestaurantId(), Boolean.TRUE)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        OrderDetailResponseDto order = orderRepository.getOrderDetails(request.getOrderId());

        if (!request.getRestaurantId().equals(order.getRestaurantId())) {
            System.out.println(request.getRestaurantId());
            System.out.println(request.getOrderId());
            System.out.println(order.getRestaurantId());

            throw new IllegalArgumentException("Sorry, it seems that you never dine in this restaurant");
        }

        if (request.getReviewContent().isEmpty()) {
            throw new InvalidException("Please enter the review");
        }

        if (request.getRating() < 1) {
            throw new InvalidException("Rating can not be lower than 1 star");
        }

        Double currentRestaurantRating = restaurant.getRating();
        Long totalRating = restaurant.getTotalRating() + 1;

        String imageUrl = (file != null && !file.isEmpty())
            ? uploadService.upload(file, UploadType.REVIEW)
            : null;

        Double ratingRestaurant = ((currentRestaurantRating + request.getRating()) / totalRating);

        AddReviewRequest review = new AddReviewRequest();
        review.setReviewContent(request.getReviewContent());
        review.setRestaurantId(restaurant.getId());
        review.setRating(request.getRating());
        review.setOrderId(request.getOrderId());
        review.setCustomerId(request.getCustomerId());

        Integer resultAddReview = 0;

        if (reviewRepository.addReview(review, imageUrl, order.getCustomerName()) == 0) {
            throw new InvalidException("Error while adding review, please check your input");
        };

        restaurantRepository.addReview(restaurant.getId(), ratingRestaurant, totalRating);

    }

}

