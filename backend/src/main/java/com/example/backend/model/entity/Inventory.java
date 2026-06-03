package com.example.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.soap.Text;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "inventory_in")
@Data
@ApiOperation("Inventory Management")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "inventoryID")
    private Integer id;

    @ApiModelProperty(value = "quantity")
    private Integer quantity;

    @JsonProperty("product_id")
    @ApiModelProperty(value = "product_id")
    private Integer product_id;

    @ApiModelProperty(value = "supplier")
    private String supplier;

    @JsonProperty("created_at")
    @ApiModelProperty(value = "created_at")
    private LocalDateTime created_at;

    @ApiModelProperty(value = "operator")
    private String operator;

    @ApiModelProperty(value = "remark")
    private String remark;

    @ApiModelProperty(value = "original_price")
    private BigDecimal original_price;
}
