package com.example.backend.mapper;

import com.example.backend.model.entity.Order;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM `order` WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> selectOrdersByUserId(@Param("userId") int userId);

    @Insert(" INSERT INTO  `order` (order_no, user_id, total_amount, pay_status, order_status,\n" +
            "        shipping_address, receiver_name, receiver_phone,  create_time, update_time)\n" +
            "    VALUES(#{order_no}, #{user_id}, #{total_amount}, 0, #{order_status},\n" +
            "        #{shipping_address}, #{receiver_name}, #{receiver_phone},  #{create_time}, #{update_time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Select("SELECT * FROM `order` WHERE order_no = #{orderId}")
    Order getOrderById(String orderId);

    @Delete("DELETE * FROM `order` WHERE order_no = #{orderId}")
    void deleteOrderById(String orderId);

    //Update Status
    @Update("UPDATE `order` SET pay_status = 1, order_status = 0 , pay_time = Now() WHERE order_no = #{orderId}")
    void updateStatus(@Param("orderId") String orderId);

    //Confirm Receive
    @Update("UPDATE `order` SET order_status = 2, complete = Now() WHERE order_no = #{orderId}")
    void ConfirmReceive(@Param("orderId") String orderId);

    //Manager
    //SentOut
    @Update("UPDATE `order` SET order_status = 1, delivery_time = Now() WHERE order_no = #{orderId}")
    void SendOutGood(@Param("orderId") String orderId);

    //Fetch All Order
    @Select("SELECT * FROM `order`")
    List<Order> selectOrders();
}
