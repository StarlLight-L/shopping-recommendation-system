package com.example.backend.model.dto;

import com.example.backend.model.entity.Product;
import java.util.List;

/**
 * DTO for paginated product response
 */
public class PaginatedProductResponse {

    private List<Product> products;
    private int totalProducts;
    private int totalPages;

    public PaginatedProductResponse() {
    }

    public PaginatedProductResponse(List<Product> products, int totalProducts, int totalPages) {
        this.products = products;
        this.totalProducts = totalProducts;
        this.totalPages = totalPages;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
