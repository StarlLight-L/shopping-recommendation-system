package com.example.backend.service;

import com.example.backend.model.dto.SearchDTO;
import com.example.backend.model.entity.History;
import com.example.backend.model.entity.Search;

public interface SearchService {

    Boolean addSearch(SearchDTO searchDTO);

    Boolean addHistory(History history);
}
