package com.example.backend.service;

import com.example.backend.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> getProductsByCategory(String category, String priceRange, String brand, int page, int limit);

    // Fetch the total number of products for pagination
    int getTotalProductCount(String category, String priceRange, String brand);

    List<Product> getRecommendedProducts(String category, int limit);

    Product getProductById(Integer id);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    Page<Product> getAllProducts(Pageable pageable);

    Boolean deleteProductById(Integer id);

    Boolean addNewProduct(Product product);

    Boolean updateProductById(Product product);
}