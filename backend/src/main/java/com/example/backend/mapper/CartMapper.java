package com.example.backend.mapper;

import com.example.backend.model.entity.CartItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    // Add Product
    @Insert("INSERT INTO cart_items (user_id, product_id, quantity, price, created_at, updated_at) " +
            "VALUES (#{userId}, #{productId}, #{quantity}, #{price}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CartItem cartItem);

    // Update Quantity
    @Update("UPDATE cart_items SET " +
            "quantity = #{quantity}, " +
            "updated_at = NOW() " +
            "WHERE id = #{cartItemId} AND user_id = #{userId}")
    int updateQuantity(@Param("userId") Integer userId, @Param("cartItemId") Integer cartItemId, @Param("quantity") Integer quantity);

    // Delete Cart
    @Delete("DELETE FROM cart_items WHERE id = #{id} AND user_id = #{userId}")
    int deleteById(@Param("id") Integer id, @Param("userId") Integer userId);

    // Clean cart
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Integer userId);

    // Fetch user cart
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "product", column = "product_id",
                one = @One(select = "com.example.backend.mapper.ProductMapper.selectById")),
        @Result(property = "userId",column = "user_id"),
        @Result(property = "productId",column = "product_id")
    })
    List<CartItem> selectByUserId(@Param("userId") Integer userId);

    // check if product exists
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}