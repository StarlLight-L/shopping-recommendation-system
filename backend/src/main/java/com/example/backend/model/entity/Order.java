package com.example.backend.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;

@Table(name = "order")
@Data
@ApiModel("Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "orderID")
    private Long id;
    @ApiModelProperty(value = "order index")
    private String order_no;
    @ApiModelProperty(value = "userID")
    private Integer user_id;
    @ApiModelProperty(value = "total_amount")
    private BigDecimal total_amount;
    @ApiModelProperty(value = "pay status")
    private Integer pay_status; // 0-Unpaid, 1-Paid, 2-Cancelled
    @ApiModelProperty(value = "order status")
    private Integer order_status; // 0-Pending Shipment, 1-Shipped, 2-Received, 3-Completed
    @ApiModelProperty(value = "address")
    private String shipping_address;
    @ApiModelProperty(value = "receiver name")
    private String receiver_name;
    @ApiModelProperty(value = "receiver phone")
    private String receiver_phone;
    @ApiModelProperty(value = "pay time")
    private LocalDateTime pay_time;
    @ApiModelProperty(value = "delivery time")
    private LocalDateTime delivery_time;
    @ApiModelProperty(value = "complete time")
    private LocalDateTime complete_time;
    @ApiModelProperty(value = "create time")
    private LocalDateTime create_time;
    @ApiModelProperty(value = "update time")
    private LocalDateTime update_time;

}
