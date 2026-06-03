package com.example.backend.controller;


import com.example.backend.model.dto.FavoriteDTO;
import com.example.backend.model.entity.Product;
import com.example.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.addToFavorites(favoriteDTO);
        return ResponseEntity.ok("Added to favorites");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.removeFromFavorites(favoriteDTO);
        return ResponseEntity.ok("Removed from favorites");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<Product>> getFavorites(@PathVariable Integer userId) {
        return ResponseEntity.ok(favoriteService.getFavorites(userId));
    }

    @GetMapping("/status")
    public Integer getFavoritesStatus(
            @RequestParam Integer userId,
            @RequestParam Integer productId
    ) {
        // Call the service layer method to get the favorite status
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setUser_id(userId);
        favoriteDTO.setProduct_id(productId);
        return favoriteService.isProductInFavorite(favoriteDTO);
    }
}

