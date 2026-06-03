package com.example.backend.controller;

import com.example.backend.model.dto.ProductDTO;
import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import com.example.backend.service.impl.ProductServiceImpl;
import com.example.backend.util.FileStorageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/products")
@Api(tags = "商品管理接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get products by category with pagination
    @GetMapping("/category/{category}")
    public PaginatedResponse getProductsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) String brand,
            @RequestParam int page,
            @RequestParam int limit) {

        List<Product> products = productService.getProductsByCategory(category, priceRange, brand, page, limit);
        int totalProducts = productService.getTotalProductCount(category, priceRange, brand);
        int totalPages = (int) Math.ceil((double) totalProducts / limit);

        return new PaginatedResponse(products, totalProducts, totalPages);
    }

    // Simple DTO for paginated response
    public static class PaginatedResponse {
        private List<Product> products;
        private int totalProducts;
        private int totalPages;

        // Constructor, Getters and Setters
        public PaginatedResponse(List<Product> products, int totalProducts, int totalPages) {
            this.products = products;
            this.totalProducts = totalProducts;
            this.totalPages = totalPages;
        }

        public List<Product> getProducts() {
            return products;
        }

        public int getTotalProducts() {
            return totalProducts;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }

    @GetMapping("/recommended")
    @ApiOperation("获取推荐商品")
    public ResponseEntity<List<Product>> getRecommendedProducts(
            @RequestParam String category,
            @RequestParam(defaultValue = "6") int limit) {
        List<Product> products = productService.getRecommendedProducts(category, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取商品详情")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //搜索商品
    @GetMapping("/search")
    @ApiOperation("搜索商品")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(keyword, pageRequest);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    @ApiOperation("获取所有商品")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Product> products = productService.getAllProducts(pageRequest);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/delete")
    @ApiOperation("Delete Products")
    public ResponseEntity<Boolean> deleteProductById(@RequestParam Integer id) {
        Boolean success = productService.deleteProductById(id);
        return ResponseEntity.ok(success);
    }

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploads/product")
    @ApiOperation("Upload image")
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile file) {
        try {
            String relativePath = fileStorageService.storeProductImage(file);
            return ResponseEntity.ok().body(relativePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("图片上传失败");
        }
    }
    //Manager
    @PostMapping("/add")
    @ApiOperation("Add a product for sale")
    public ResponseEntity<Boolean> insertProduct(@RequestBody Product product) {
        System.out.println(" Received tags: " + product.getTags());
        Boolean success = productService.addNewProduct(product);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/update")
    @ApiOperation("Update product information")
    public ResponseEntity<Boolean> updateProduct(@RequestBody Product product) {
        Boolean success = productService.updateProductById(product);
        return ResponseEntity.ok(success);
    }
}