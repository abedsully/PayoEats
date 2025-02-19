package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Image findByMenu(Menu menu);
}
