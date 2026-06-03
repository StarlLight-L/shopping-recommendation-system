package com.example.backend.service.impl;

import com.example.backend.mapper.CartMapper;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.dto.CartDTO;
import com.example.backend.model.entity.CartItem;
import com.example.backend.model.entity.Product;
import com.example.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public CartItem addToCart(CartDTO cartDTO) {
        //
        int productId = cartDTO.getProduct_id();
        int quantity = cartDTO.getQuantity();
        int userId = cartDTO.getUser_id();
        // check if product exist
        Product product = productMapper.selectById(cartDTO.getProduct_id());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check if the stock is sufficient
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient product stock");
        }

        // Check if the product already exists in the cart
        CartItem existingItem = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (existingItem != null) {
            // Update Quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            cartMapper.updateQuantity(existingItem.getId(), userId, newQuantity);
            existingItem.setQuantity(newQuantity);
            return existingItem;
        }

        // Create a new cart item
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice());

        cartMapper.insert(cartItem);
        return cartItem;
    }

    @Override
    @Transactional
    public boolean updateQuantity(Integer userId, Integer cartItemId, Integer quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, cartItemId);
        }

        // Update Quantity
        return cartMapper.updateQuantity(userId, cartItemId, quantity) > 0;
    }

    @Override
    @Transactional
    public boolean removeFromCart(Integer userId, Integer cartItemId) {
        return cartMapper.deleteById(cartItemId, userId) > 0;
    }

    @Override
    @Transactional
    public boolean clearCart(Integer userId) {
        return cartMapper.deleteByUserId(userId) > 0;
    }

    @Override
    public List<CartItem> getUserCart(Integer userId) {
        return cartMapper.selectByUserId(userId);
    }

    @Override
    public CartItem findCartItem(Integer userId, Integer productId) {
        return cartMapper.selectByUserIdAndProductId(userId, productId);
    }
}