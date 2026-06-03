package com.example.backend.service.impl;

import com.example.backend.mapper.DeviceMapper;
import com.example.backend.model.dto.DeviceDTO;
import com.example.backend.model.dto.updateDeviceDTO;
import com.example.backend.model.entity.Devices;
import com.example.backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public boolean addUserDevice(DeviceDTO deviceDTO) {
        Devices device = new Devices();
        device.setUser_id(deviceDTO.getUser_id());
        device.setDevice_name(deviceDTO.getDevice_name());
        device.setDevice_type(deviceDTO.getDevice_type());
        device.setDevice_model(deviceDTO.getDevice_model());
        device.setDevice_add_time(LocalDateTime.now());
        log.print(device);

        deviceMapper.insertDevice(device);
        return true;
    }

    @Override
    @Transactional
    public List<Devices> getDevicesByUserId(int userId) {
        return deviceMapper.selectDevicesByUserId(userId);
    }

    @Override
    public boolean deleteDeviceById(int id) {
        int rows = deviceMapper.deleteDeviceById(id);
        if (rows == 0) {
            throw new RuntimeException("Device not found or deletion failed");
        }
        return true;
    }

    @Override
    public boolean updateDevice(updateDeviceDTO dto) {
        int rows = deviceMapper.updateDevice(dto);
        if (rows == 0) {
            throw new RuntimeException("Device not found or update failed");
        }
        return true;
    }
}
