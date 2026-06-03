package com.example.backend.model.entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Table(name = "order_item")
@Data
@ApiModel("OrderItem")
public class OrderItem {
    @Id
    @ApiModelProperty(value = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "orderID")
    private String order_id;
    @ApiModelProperty(value = "product ID")
    private Long product_id;
    @ApiModelProperty(value = "product name")
    private String product_name;
    @ApiModelProperty(value = "product image url")
    private String product_image;
    @ApiModelProperty(value = "product price")
    private BigDecimal product_price;
    @ApiModelProperty(value = "quantity")
    private Integer quantity;
    @ApiModelProperty(value = "total price")
    private BigDecimal total_price;
    @ApiModelProperty(value = "create time")
    private LocalDateTime create_time;
}
