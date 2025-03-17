package com.example.PayoEat_BE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] image;
    private String downloadUrl;

    private UUID menuCode;

    private UUID restaurantId;
}
