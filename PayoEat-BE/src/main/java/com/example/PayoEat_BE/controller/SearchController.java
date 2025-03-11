package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.SearchResultDto;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.search.ISearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final ISearchService searchService;

    @PostMapping("/restaurant-menu")
    @Operation(summary = "Searching restaurant and menu", description = "Returning list of restaurant")
    public ResponseEntity<ApiResponse> searchRestaurant(@RequestParam String query) {
        try {
            SearchResultDto results = searchService.search(query);
            return ResponseEntity.ok(new ApiResponse("Search result: ", results));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
