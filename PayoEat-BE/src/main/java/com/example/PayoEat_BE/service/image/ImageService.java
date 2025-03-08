package com.example.PayoEat_BE.service.image;

import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;

    @Override
    public Image saveImage(MultipartFile file, String menuCode) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());

            Image savedImage = imageRepository.save(image);
            savedImage.setDownloadUrl("/menu/download/" + savedImage.getId());
            return imageRepository.save(savedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image: " + e.getMessage());
        }
    }

    public Image getImageById(UUID imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
    }

    @Override
    public Image updateImage(MultipartFile file, UUID id) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File can not be empty");
        }

        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Image not found with id: " + id));

            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());
            image.setDownloadUrl("/menu/download/" + image.getId());

            return imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage());
        }
    }

}
