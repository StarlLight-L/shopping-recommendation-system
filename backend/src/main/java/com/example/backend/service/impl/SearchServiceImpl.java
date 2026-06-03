package com.example.backend.service.impl;

import com.example.backend.mapper.ServiceMapper;
import com.example.backend.model.dto.SearchDTO;
import com.example.backend.model.entity.History;
import com.example.backend.model.entity.Search;
import com.example.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ServiceMapper serviceMapper;

    @Override
    public Boolean addSearch(SearchDTO searchDTO) {
        serviceMapper.addSearch(searchDTO);
        serviceMapper.deleteOldSearchHistory(searchDTO.getUser_id());
        return true;
    }

    @Override
    public Boolean addHistory(History history) {
        serviceMapper.addHistory(history);
        serviceMapper.deleteOldHistory(history.getUser_id());
        return true;
    }
}
