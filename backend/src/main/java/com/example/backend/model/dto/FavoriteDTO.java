package com.example.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("FavoriteDTO")
public class FavoriteDTO {
    @NotBlank(message = "UserID")
    @JsonProperty("userId")
    private Integer user_id;

    @NotBlank(message = "ProductID")
    @JsonProperty("productId")
    private Integer product_id;

}

