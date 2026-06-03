package com.example.backend.model.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Table(name = "user_address")
@Data
@ApiModel("Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private int id;
    @ApiModelProperty(value = "userID")
    private int user_id;
    @ApiModelProperty(value = "consignee")
    private String consignee;
    @ApiModelProperty(value = "phone")
    private String phone;
    @ApiModelProperty(value = "province")
    private String province;
    @ApiModelProperty(value = "city")
    private String city;
    @ApiModelProperty(value = "district")
    private String district;
    @ApiModelProperty(value = "detail")
    private String detail;
    @ApiModelProperty(value = "default address")
    private Boolean is_default;


}
