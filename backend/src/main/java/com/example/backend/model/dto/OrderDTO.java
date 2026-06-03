package com.example.backend.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
@Data
@ApiModel("OrderDTO")
public class OrderDTO {

    @ApiModelProperty(value = "Order Number")
    private String order_no;

    @NotNull(message = "User ID cannot be null")
    @ApiModelProperty(value = "User ID", required = true)
    private Integer user_id;

    @NotNull(message = "Total amount cannot be null")
    @ApiModelProperty(value = "Total Amount", required = true)
    private BigDecimal total_amount;

    @ApiModelProperty(value = "Payment Status: 0-Unpaid, 1-Paid, 2-Cancelled")
    private Integer pay_status;

    @ApiModelProperty(value = "Order Status: 0-Pending Shipment, 1-Shipped, 2-Received, 3-Completed")
    private Integer order_status;

    @NotBlank(message = "Shipping address cannot be empty")
    @ApiModelProperty(value = "Shipping Address", required = true)
    private String shipping_address;

    @NotBlank(message = "Receiver name cannot be empty")
    @ApiModelProperty(value = "Receiver Name", required = true)
    private String receiver_name;

    @NotBlank(message = "Receiver phone cannot be empty")
    @ApiModelProperty(value = "Receiver Phone", required = true)
    private String receiver_phone;

    @ApiModelProperty(value = "Payment Time")
    private LocalDateTime pay_time;

    @ApiModelProperty(value = "Delivery Time")
    private LocalDateTime delivery_time;

    @ApiModelProperty(value = "Completion Time")
    private LocalDateTime complete_time;

    @NotEmpty(message = "Order items cannot be empty")
    @ApiModelProperty(value = "Order Item List", required = true)
    private List<OrderItemDTO> items;

}