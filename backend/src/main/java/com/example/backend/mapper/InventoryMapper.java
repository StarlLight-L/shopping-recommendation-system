package com.example.backend.mapper;

import com.example.backend.model.entity.Inventory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InventoryMapper {

    @Select("SELECT * from `inventory_in`")
    List<Inventory> getInventoryInList();

    @Insert("INSERT INTO `inventory_in` (product_id, quantity, original_price, supplier, created_at, operator, remark)" +
            "VALUES (#{product_id}, #{quantity}, #{original_price}, #{supplier}, Now(), #{operator}, #{remark})")
    void InventoryAdd(Inventory inventory);

    @Delete("DELETE FROM `inventory_in` WHERE id = #{id}")
    void InventoryDelete(@Param("productId") Integer productId);
}
