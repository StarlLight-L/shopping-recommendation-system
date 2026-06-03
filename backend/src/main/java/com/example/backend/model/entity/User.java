package com.example.backend.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Table(name = "users")
@Entity
@Data
@ApiModel("User info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "userID")
    private int id;
    @ApiModelProperty(value = "user name")
    private String user_name;
    @ApiModelProperty(value = "user email")
    private String user_email;
    @ApiModelProperty(value = "user password")
    private String user_password;
    @ApiModelProperty(value = "user avatar")
    private String user_avatar;
    @ApiModelProperty(value = "user phone")
    private String user_phone;
    @ApiModelProperty(value = "user avatar")
    private String user_birthday;
    @ApiModelProperty(value = "user phone")
    private String user_gender;
    @ApiModelProperty(value = "user create time")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "user update time")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "last login time")
    private LocalDateTime lastLoginTime;

}
