package com.example.backend.model.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "cart_items")
@Data
@ApiModel("CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Cart Item ID")
    private Integer id;

    @Column(name = "user_id")
    @ApiModelProperty(value = "User ID")
    private Integer userId;

    @Column(name = "product_id")
    @ApiModelProperty(value = "Product ID")
    private Integer productId;

    @ApiModelProperty(value = "Quantity")
    private Integer quantity;

    @ApiModelProperty(value = "Product Price")
    private BigDecimal price;

    @ApiModelProperty(value = "Created Time")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "Updated Time")
    private LocalDateTime updatedAt;

    // Entity relationship mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}