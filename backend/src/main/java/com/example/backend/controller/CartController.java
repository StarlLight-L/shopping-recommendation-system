package com.example.backend.controller;

import com.example.backend.model.dto.CartDTO;
import com.example.backend.model.entity.CartItem;
import com.example.backend.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@RestController
@RequestMapping("/api/user/cart")
@Api(tags = "Cart API")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @ApiOperation("Add product to cart")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartDTO cartDTO) {
        CartItem cartItem = cartService.addToCart(cartDTO);
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/update")
    @ApiOperation("Update CartItem Number")
    public ResponseEntity<Boolean> updateQuantity(
            @RequestParam Integer userId,
            @RequestParam Integer cartItemId,
            @RequestParam Integer quantity
            ) {
        boolean success = cartService.updateQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/remove")
    @ApiOperation("Remove a product from the cart")
    public ResponseEntity<Boolean> removeFromCart(
            @RequestParam Integer userId,
            @RequestParam Integer cartItemId) {
        boolean success = cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/clear")
    @ApiOperation("Clear the user's cart")
    public ResponseEntity<Boolean> clearCart(@RequestParam Integer userId) {
        boolean success = cartService.clearCart(userId);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("Get the user's cart items")
    public ResponseEntity<List<CartItem>> getUserCart(@PathVariable Integer userId) {
        List<CartItem> cartItems = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartItems);
    }
}