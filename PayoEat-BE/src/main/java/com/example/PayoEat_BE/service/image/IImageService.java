package com.example.PayoEat_BE.service.image;

import com.example.PayoEat_BE.dto.ImageDto;
import com.example.PayoEat_BE.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IImageService {
    Image saveImage(MultipartFile file, UUID menuCode);
    Image getImageById(UUID imageId);
    Image updateImage(MultipartFile file, UUID id);
}
