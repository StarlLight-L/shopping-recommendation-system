package com.example.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Add DeviceDTO")
public class DeviceDTO {
    @NotBlank(message = "UserID")
    private Integer user_id;

    @NotBlank(message = "DeviceName")
    private String device_name;

    @NotBlank(message = "DeviceType")
    private String device_type;

    @NotBlank(message = "DeviceModel")
    private String device_model;
}
