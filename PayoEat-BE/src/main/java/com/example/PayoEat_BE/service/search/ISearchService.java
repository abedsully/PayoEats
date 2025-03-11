package com.example.PayoEat_BE.service.search;

import com.example.PayoEat_BE.dto.SearchResultDto;

public interface ISearchService {
    SearchResultDto search(String query);
}
