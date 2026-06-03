package com.example.backend.service;

import com.example.backend.model.dto.DeviceDTO;
import com.example.backend.model.dto.updateDeviceDTO;
import com.example.backend.model.entity.Devices;

import java.util.List;
public interface DeviceService {

    boolean addUserDevice(DeviceDTO deviceDTO);

    List<Devices> getDevicesByUserId(int userId);

    boolean deleteDeviceById(int id);

    boolean updateDevice(updateDeviceDTO dto);
}
