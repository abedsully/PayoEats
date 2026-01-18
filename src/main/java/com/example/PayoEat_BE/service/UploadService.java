package com.example.PayoEat_BE.service;

import com.example.PayoEat_BE.enums.UploadType;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.UUID;

@Service
public class UploadService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket1}")
    private String bucket1;

    @Value("${supabase.bucket2}")
    private String bucket2;

    @Value("${supabase.bucket3}")
    private String bucket3;

    @Value("${supabase.bucket4}")
    private String bucket4;

    @Value("${supabase.bucket5}")
    private String bucket5;


    private final WebClient webClient;

    public UploadService() {
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE);

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public String upload(MultipartFile file, UploadType type) {
        try {
            String original = file.getOriginalFilename();

            String safeName = original
                    .replaceAll("[^A-Za-z0-9._-]", "-")
                    .replaceAll("-+", "-");

            String fileName = UUID.randomUUID() + "-" + safeName;

            String bucketPath = "";

            switch (type) {
                case PAYMENT:
                    bucketPath = bucket1;
                    break;
                case REVIEW:
                    bucketPath = bucket2;
                    break;
                case RESTAURANT:
                    bucketPath = bucket3;
                    break;
                case QR:
                    bucketPath = bucket4;
                    break;
                case MENU:
                    bucketPath = bucket5;
                    break;
                default:
                    bucketPath = bucket2;
            }
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketPath + "/" + fileName;

            String response = webClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("Content-Type", file.getContentType())
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Upload failed: " + body)))
                    .bodyToMono(String.class)
                    .block();

            return supabaseUrl + "/storage/v1/object/public/" + bucketPath + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }

    public String update(MultipartFile file, UploadType type, String oldFileUrl) {
        try {
            String bucketPath = "";

            switch (type) {
                case PAYMENT:
                    bucketPath = bucket1;
                    break;
                case REVIEW:
                    bucketPath = bucket2;
                    break;
                case RESTAURANT:
                    bucketPath = bucket3;
                    break;
                case QR:
                    bucketPath = bucket4;
                    break;
                case MENU:
                    bucketPath = bucket5;
                    break;
                default:
                    bucketPath = bucket2;
            }

            String oldFileName = extractFileName(oldFileUrl);

            if (oldFileName != null) {
                delete(type, oldFileName);
            }

            String original = file.getOriginalFilename();

            String safeName = original
                    .replaceAll("[^A-Za-z0-9._-]", "-")
                    .replaceAll("-+", "-");

            String newFileName = UUID.randomUUID() + "-" + safeName;

            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketPath + "/" + newFileName;

            webClient.post()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("Content-Type", file.getContentType())
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            resp -> resp.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Upload failed: " + body))
                    )
                    .bodyToMono(Void.class)
                    .block();

            return supabaseUrl + "/storage/v1/object/public/" + bucketPath + "/" + newFileName;

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        }
    }



    public void delete(UploadType type, String fileName) {
        try {

            String bucketPath = "";

            switch (type) {
                case PAYMENT:
                    bucketPath = bucket1;
                    break;
                case REVIEW:
                    bucketPath = bucket2;
                    break;
                case RESTAURANT:
                    bucketPath = bucket3;
                    break;
                case QR:
                    bucketPath = bucket4;
                    break;
                case MENU:
                    bucketPath = bucket5;
                    break;
                default:
                    bucketPath = bucket2;
            }

            String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucketPath + "/" + fileName;

            webClient.delete()
                    .uri(deleteUrl)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
    }

    private String extractFileName(String url) {
        if (url == null || url.isBlank()) return null;

        if (!url.contains("/storage/v1/object/public/")) {
            return null;
        }

        return url.substring(url.lastIndexOf("/") + 1);
    }



}