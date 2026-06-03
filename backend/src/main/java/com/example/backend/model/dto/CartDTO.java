package com.example.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Add CartDTO")
public class CartDTO {
    @NotBlank(message = "UserID")
    @JsonProperty("userId")
    private Integer user_id;

    @JsonProperty("productId")
    @NotBlank(message = "ProductID")
    private Integer product_id;

    @Email(message = "Product Quantity")
    private Integer quantity;
}
