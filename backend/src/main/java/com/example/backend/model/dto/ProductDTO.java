package com.example.backend.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.spring.web.json.Json;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.soap.Text;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("ProductDTO")
public class ProductDTO {
    @ApiModelProperty(value = "Product ID")
    private Integer id;

    @NotBlank(message = "Product name cannot be empty")
    @ApiModelProperty(value = "Product Name", required = true)
    private String name;

    @ApiModelProperty(value = "Product Description")
    private Text description;

    @NotNull(message = "Product price cannot be null")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    @ApiModelProperty(value = "Product Price", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "Original Price")
    private BigDecimal original_price;

    @NotBlank(message = "Product category cannot be empty")
    @ApiModelProperty(value = "Category", required = true)
    private String category;

    @ApiModelProperty(value = "Product Image URL")
    private String image_url;

    @NotNull(message = "Stock cannot be null")
    @ApiModelProperty(value = "Stock", required = true)
    private Integer stock;

    @ApiModelProperty(value = "Product Tags")
    private Json tags;

    @ApiModelProperty(value = "Created Time")
    private LocalDateTime created_at;

    @ApiModelProperty(value = "Updated Time")
    private LocalDateTime updated_at;
}