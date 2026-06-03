package com.example.backend.model.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.spring.web.json.Json;
import javax.persistence.*;

@Table(name = "favorite")
@Data
@ApiModel("Favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private int id;
    @Column(name = "user_id")
    @ApiModelProperty(value = "userID")
    private int userId;
    @Column(name = "product_id")
    @ApiModelProperty(value = "productID")
    private int productId;
    @ApiModelProperty(value = "is deleted")
    private Boolean is_deleted;
    @ApiModelProperty(value = "user create time")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "user update time")
    private LocalDateTime updateTime;


    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

}
