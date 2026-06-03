package com.example.backend.controller;

import com.example.backend.model.dto.DeviceDTO;
import com.example.backend.model.dto.updateDeviceDTO;
import com.example.backend.model.entity.Devices;

import com.example.backend.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/device")
@Api(tags="Device API")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    @ApiOperation("add devices")
    public ResponseEntity<Boolean> addUserDevice(@RequestBody DeviceDTO deviceDTO) {
        boolean success = deviceService.addUserDevice(deviceDTO);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("Get the device list by user ID")
    public ResponseEntity<List<Devices>> getDevicesByUserId(@PathVariable int userId) {
        List<Devices> devices = deviceService.getDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Delete a device by ID")
    public ResponseEntity<Boolean> deleteDevice(@PathVariable int id) {
        boolean success = deviceService.deleteDeviceById(id);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/update")
    @ApiOperation("Update device information")
    public ResponseEntity<Boolean> updateDevice(@RequestBody updateDeviceDTO dto) {
        boolean success = deviceService.updateDevice(dto);
        return ResponseEntity.ok(success);
    }
}
