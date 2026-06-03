package com.example.backend.service.impl;

import com.example.backend.mapper.FavoriteMapper;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.dto.FavoriteDTO;
import com.example.backend.model.entity.Product;
import com.example.backend.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addToFavorites(FavoriteDTO dto) {
        Integer userId = dto.getUser_id();
        Integer productId = dto.getProduct_id();
        Integer exists = favoriteMapper.exists(userId, productId);
        if (exists != null) {
            throw new RuntimeException("Product is already in favorites");
        }

        // 插入收藏记录
        favoriteMapper.insert(userId, productId);
    }

    @Override
    public void removeFromFavorites(FavoriteDTO dto) {
        Integer userId = dto.getUser_id();
        Integer productId = dto.getProduct_id();
        log.print(dto);
        Integer exists = favoriteMapper.exists(userId, productId);
        if (exists == null) {
            throw new RuntimeException("Product is not in favorites");
        }

        favoriteMapper.delete(userId, productId);
    }

    @Override
    public List<Product> getFavorites(Integer userId) {
        List<Integer> productIds = favoriteMapper.findProductIdsByUserId(userId);
        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }
        return productMapper.selectByIds(productIds);
    }

    @Override
    public Integer isProductInFavorite(FavoriteDTO favoriteDTO){
        Integer userId = favoriteDTO.getUser_id();
        Integer productId = favoriteDTO.getProduct_id();
        Integer exists = favoriteMapper.exists(userId, productId);
        return exists;
    }
}
