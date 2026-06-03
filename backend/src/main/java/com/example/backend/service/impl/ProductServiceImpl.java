package com.example.backend.service.impl;

import org.json.JSONObject;
import com.example.backend.model.entity.Product;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getProductsByCategory(String category, String priceRange, String brand, int page, int limit) {
        Integer minPrice = null;
        Integer maxPrice = null;

        if (priceRange != null && !"null".equals(priceRange)) {
            try {
                JSONObject priceObj = new JSONObject(priceRange);
                minPrice = priceObj.getInt("min");
                maxPrice = priceObj.getInt("max");
            } catch (Exception e) {
                minPrice = 0;
                maxPrice = 100000;
            }
        }

        if ("null".equals(brand)) {
            brand = null;
        }
        int offset = (page - 1) * limit;
        return productMapper.getProductsByCategory(category, minPrice, maxPrice, brand, offset, limit);
    }

    @Override
    public int getTotalProductCount(String category, String priceRange, String brand) {
        return productMapper.getTotalProductCount(category, priceRange, brand);
    }

    @Override
    public List<Product> getRecommendedProducts(String category, int limit) {
        return Collections.emptyList();
    }

    //@Override
    //public List<Product> getRecommendedProducts() {
    //}

    @Override
    public Product getProductById(Integer id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return product;
    }

    //搜索
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        System.out.println("Keyword：" + keyword);
        List<Product> products = productMapper.selectByKeyword(keyword, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        long total = productMapper.countBySearch(keyword);
        System.out.println("Product Quantity：" + total);
        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        List<Product> products = productMapper.selectAll();
        long total = productMapper.count();
        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public Boolean deleteProductById(Integer id) {
        productMapper.deleteById(id);
        return true;
    }

    @Override
    public Boolean addNewProduct(Product product) {
        productMapper.insert(product);
        return true;
    }

    @Override
    public Boolean updateProductById(Product product) {
        productMapper.update(product);
        return true;
    }

}