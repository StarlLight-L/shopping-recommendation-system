package com.example.backend.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.spring.web.json.Json;
import javax.persistence.*;

@Table(name = "products")
@Data
@ApiModel("Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "productID")
    private int id;
    @ApiModelProperty(value = "product name")
    private String name;
    @ApiModelProperty(value = "product description")
    private String description;
    @ApiModelProperty(value = "product price")
    private BigDecimal price;
    @ApiModelProperty(value = "product original_price")
    private BigDecimal original_price;
    @ApiModelProperty(value = "product category")
    private String category;
    @ApiModelProperty(value = "product image_url")
    private String image_url;
    @ApiModelProperty(value = "product stock")
    private int stock;
    @Column(name = "tags")
    @ApiModelProperty(value = "product tags")
    private String tags;
    @ApiModelProperty(value = "product create time")
    private LocalDateTime created_at;
    @ApiModelProperty(value = "product update time")
    private LocalDateTime updated_at;
}
