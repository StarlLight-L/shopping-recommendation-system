package com.example.backend.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel("UserDTO")
public class UserDTO {
    @ApiModelProperty(value = "User ID")
    private Integer id;

    @NotBlank(message = "Username cannot be empty")
    @ApiModelProperty(value = "Username", required = true)
    private String userName;

    @ApiModelProperty(value = "User Avatar")
    private String userAvatar;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    @ApiModelProperty(value = "Phone Number")
    private String userPhone;

    @ApiModelProperty(value = "Created Time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "Updated Time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "Last Login Time")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "Gender")
    private String userGender;

    @ApiModelProperty(value = "Date of Birth")
    private String userBirthday;
}