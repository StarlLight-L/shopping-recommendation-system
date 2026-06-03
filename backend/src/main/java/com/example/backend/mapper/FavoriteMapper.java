package com.example.backend.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO favorite (user_id, product_id) VALUES (#{userId}, #{productId})")
    void insert(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    void delete(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Select("SELECT product_id FROM favorite WHERE user_id = #{userId}")
    List<Integer> findProductIdsByUserId(@Param("userId") Integer userId);

    @Select("SELECT 1 FROM favorite WHERE user_id = #{userId} AND product_id = #{productId} LIMIT 1")
    Integer exists(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
