package com.example.backend.mapper;

import com.example.backend.model.dto.ProductDTO;
import com.example.backend.model.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // Insert New product
    @Insert("INSERT INTO products (name, description, price, stock, category, image_url,tags, created_at, updated_at) " +
            "VALUES (#{name}, #{description}, #{price}, #{stock}, #{category}, #{image_url},#{tags}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    // Update product info
    @Update("UPDATE products SET " +
            "name = #{name}, " +
            "description = #{description}, " +
            "price = #{price}, " +
            "stock = #{stock}, " +
            "category = #{category}, " +
            "image_url = #{image_url}, " +
            "updated_at = NOW() " +
            "WHERE name = #{name}")
    int update(Product product);

    // Delete product
    @Delete("DELETE FROM products WHERE id = #{id}")
    void deleteById(Integer id);

    // fetch product info by ID
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product selectById(@Param("id") Integer id);
    
    // fetch Quantity
    @Select("SELECT COUNT(*) FROM products")
    int count();

    // Fetch all Product
    @Select("SELECT * FROM products")
    List<Product> selectAll();

    //Fetch classification
    List<Product> getProductsByCategory(
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("brand") String brand,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // Fetch total count of products in a category
    int getTotalProductCount(
            @Param("category") String category,
            @Param("priceRange") String priceRange,
            @Param("brand") String brand
    );



    // Count quantity of product
    @Select("SELECT COUNT(*)\n" +
            "    FROM products\n" +
            "    WHERE name LIKE CONCAT('%', #{keyword}, '%')" + "OR description LIKE CONCAT('%', #{keyword}, '%')")
    long countBySearch(@Param("keyword") String keyword);



    @Select("<script>SELECT * FROM products WHERE id IN "
            + "<foreach item='id' collection='list' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach></script>")
    List<Product> selectByIds(@Param("list") List<Integer> ids);



    //  Get products with pagination
    @Select("SELECT *\n" +
            "    FROM products\n" +
            "    WHERE name LIKE CONCAT('%', #{keyword}, '%')\n" +
            "    LIMIT #{offset}, #{limit}")
    List<Product> selectByKeyword(@Param("keyword") String keyword,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    @Update("UPDATE products SET " +
            "stock = stock + #{quantity} " +
            "WHERE id = #{product_id}")
    void updateStock(@Param("product_id") Integer product_id,
                     @Param("quantity") Integer quantity);

}