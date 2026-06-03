package com.example.backend.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("OrderItemDTO")
public class OrderItemDTO {

    @NotNull(message = "Order ID cannot be null")
    @ApiModelProperty(value = "Order ID", required = true)
    private String order_id;

    @NotNull(message = "Product ID cannot be null")
    @ApiModelProperty(value = "Product ID", required = true)
    private Long productId;

    @NotNull(message = "Product name cannot be null")
    @ApiModelProperty(value = "Product Name", required = true)
    private String productName;

    @NotNull(message = "Product image cannot be null")
    @ApiModelProperty(value = "Product Image", required = true)
    private String productImage;

    @NotNull(message = "Product price cannot be null")
    @ApiModelProperty(value = "Product Price", required = true)
    private BigDecimal productPrice;

    @NotNull(message = "Quantity cannot be null")
    @ApiModelProperty(value = "Quantity", required = true)
    private Integer quantity;

    @NotNull(message = "Subtotal cannot be null")
    @ApiModelProperty(value = "Subtotal", required = true)
    private BigDecimal totalPrice;
}
