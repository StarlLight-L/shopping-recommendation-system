package com.example.backend.controller;

import com.example.backend.model.dto.SearchDTO;
import com.example.backend.model.entity.History;
import com.example.backend.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@Api(tags = "搜索")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/add")
    @ApiOperation("Add a search record")
    public ResponseEntity<Boolean> addToCart(@RequestBody SearchDTO searchDTO) {
        Boolean success = searchService.addSearch(searchDTO);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/add/history")
    @ApiOperation("Add read history")
    public ResponseEntity<Boolean> addToHistory(@RequestBody History history) {
        Boolean success = searchService.addHistory(history);
        return ResponseEntity.ok(success);
    }

}
