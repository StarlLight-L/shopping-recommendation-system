package com.example.backend.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Table(name = "devices")
@Data
@ApiModel("Device")
public class Devices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private int id;
    @ApiModelProperty(value = "userID")
    private int user_id;
    @ApiModelProperty(value = "device name")
    private String device_name;
    @ApiModelProperty(value = "device type")
    private String device_type;
    @ApiModelProperty(value = "device model")
    private String device_model;
    @ApiModelProperty(value = "device add time")
    private LocalDateTime device_add_time;
}
