package com.example.backend.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("UpdateDeviceDTO")
public class updateDeviceDTO {
    @NotNull(message = "Device ID cannot be null")
    private Integer id;

    @NotBlank(message = "Device name cannot be empty")
    private String device_name;

    @NotBlank(message = "Device type cannot be empty")
    private String device_type;

    @NotBlank(message = "Device model cannot be empty")
    private String device_model;
}
