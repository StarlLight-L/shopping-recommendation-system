package com.example.backend.mapper;
import com.example.backend.model.dto.updateDeviceDTO;
import com.example.backend.model.entity.Devices;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceMapper {
    //Add Devices
    @Insert("INSERT INTO devices (user_id,device_name,device_type,device_model,device_add_time) " +
            "VALUES (#{user_id},#{device_name},#{device_type},#{device_model},Now())")
    void insertDevice(Devices devices);

    //Search user device
    @Select("SELECT * FROM devices WHERE user_id = #{user_id}")
    List<Devices> selectDevicesByUserId(@Param("user_id") int user_id);

    //Update devices
    @Update("UPDATE devices SET " +
            "device_name = #{device_name}," +
            "device_type = #{device_type}," +
            "device_model = #{device_model}," +
            "device_add_time = Now() " +
            "WHERE id = #{id}")
    int updateDevice(updateDeviceDTO updateDeviceDTO);

    //delete device
    @Delete("DELETE FROM devices WHERE id = #{id}")
    int deleteDeviceById(int id);
}
