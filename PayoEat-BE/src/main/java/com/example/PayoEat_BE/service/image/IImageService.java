package com.example.PayoEat_BE.service.image;

import com.example.PayoEat_BE.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IImageService {
    Image saveMenuImage(MultipartFile file, UUID menuCode);
    Image saveRestaurantImage(MultipartFile file, UUID restaurantId);
    Image getImageById(UUID imageId);
    Image updateImage(MultipartFile file, UUID id);
    Image saveQrisImage(MultipartFile file, UUID restaurantId);
    Image savePaymentProofImage(MultipartFile file, UUID orderId);
    Image saveReviewImage(MultipartFile file, UUID reviewId);
}
