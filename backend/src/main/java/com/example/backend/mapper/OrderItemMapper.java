package com.example.backend.mapper;

import com.example.backend.model.entity.OrderItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @Insert("INSERT INTO order_item (order_id, product_id, product_name, product_image,\n" +
            "        product_price, quantity, total_price, create_time)\n" +
            "    VALUES(#{order_id}, #{product_id}, #{product_name}, #{product_image},\n" +
            "        #{product_price}, #{quantity}, #{total_price}, #{create_time})")
    void insert(OrderItem item);

    @Select("SELECT * FROM `order_item` WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(String orderId);

    @Delete("DELETE * FROM `order_item` WHERE order_id = #{orderId}")
    void deleteOrderById(String orderId);

    @Select("SELECT * FROM `order_item` ")
    List<OrderItem> selectAllOrdersItems();
}
