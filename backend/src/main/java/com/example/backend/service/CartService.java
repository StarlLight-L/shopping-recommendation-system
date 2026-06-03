package com.example.backend.service;

import com.example.backend.model.entity.CartItem;
import java.util.List;
import com.example.backend.model.dto.CartDTO;

public interface CartService {

    CartItem addToCart(CartDTO cartDTO);

    boolean updateQuantity(Integer userId, Integer cartItemId, Integer quantity);

    boolean removeFromCart(Integer userId, Integer cartItemId);

    boolean clearCart(Integer userId);

    List<CartItem> getUserCart(Integer userId);

    CartItem findCartItem(Integer userId, Integer productId);
}