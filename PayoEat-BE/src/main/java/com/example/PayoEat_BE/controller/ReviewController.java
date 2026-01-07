package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.RestaurantReviewStatsDto;
import com.example.PayoEat_BE.dto.ReviewDto;
import com.example.PayoEat_BE.model.Review;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.review.AddReviewRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.review.IReviewService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
@Tag(name = "Review Controller", description = "Endpoint for managing reviews")
public class ReviewController {
    private final IReviewService reviewService;
    private final IUserService userService;

    @PostMapping("/add")
    @Operation(summary = "Add Review", description = "Add a review to a restaurant")
    public ResponseEntity<ApiResponse> addReview(@RequestParam("reviewContent") String reviewContent,
                                                 @RequestParam("customerId") String customerId,
                                                 @RequestParam("restaurantId") UUID restaurantId,
                                                 @RequestParam("rating") Double rating,
                                                 @RequestParam("orderId") UUID orderId,
                                                 @RequestParam(value = "reviewImageUrl", required = false) MultipartFile file) {
        try {
            AddReviewRequest request = new AddReviewRequest(reviewContent, restaurantId, rating, orderId, customerId);
            reviewService.addReview(request, file);
            return ResponseEntity.ok(new ApiResponse("Review added successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get")
    @Operation(summary = "Get Reviews by Restaurant Id", description = "Getting reviews with restaurant id")
    public ResponseEntity<ApiResponse> getReviewsByRestaurantId(@RequestParam UUID id) {
        try {
            List<ReviewDto> reviewList = reviewService.getReviewsByRestaurantId(id);

            if(reviewList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No reviews yet: ", null));
            }

            return ResponseEntity.ok(new ApiResponse("Review lists: ", reviewList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-restaurant-review-stats")
    @Operation(summary = "Get Reviews by Restaurant Id", description = "Getting reviews with restaurant id")
    public ResponseEntity<ApiResponse> getRestaurantReviewStats(@RequestParam UUID id) {
        try {
            RestaurantReviewStatsDto result = reviewService.getRestaurantReviewStats(id);
            return ResponseEntity.ok(new ApiResponse("Review lists: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get-user-review/{userId}")
    @Operation(summary = "Get reviews by user id", description = "Getting reviews bt user")
    public ResponseEntity<ApiResponse> getReviewsByUserId(@PathVariable Long userId) {
        try {
            List<Review> reviewList = reviewService.getReviewsByUserId(userId);
            List<ReviewDto> convertedReview = reviewService.getConvertedReviews(reviewList);

            if(reviewList.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No reviews yet: ", null));
            }

            return ResponseEntity.ok(new ApiResponse("Review lists: ", convertedReview));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
