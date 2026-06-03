package com.example.backend.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "read_history")
@Data
@ApiModel("History")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private int id;

    @ApiModelProperty(value = "UserID")
    private int user_id;

    @ApiModelProperty(value = "ProductID")
    private int product_id;

    @ApiModelProperty(value = "Browse time")
    private LocalDateTime time;
}
