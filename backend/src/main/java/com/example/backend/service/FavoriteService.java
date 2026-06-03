package com.example.backend.service;

import com.example.backend.model.dto.FavoriteDTO;
import com.example.backend.model.entity.Product;

import java.util.List;

public interface FavoriteService {

    void addToFavorites(FavoriteDTO dto);

    void removeFromFavorites(FavoriteDTO dto);

    List<Product> getFavorites(Integer userId);

    Integer isProductInFavorite(FavoriteDTO dto);
}

