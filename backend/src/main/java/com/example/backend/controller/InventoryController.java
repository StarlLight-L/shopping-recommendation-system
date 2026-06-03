package com.example.backend.controller;

import com.example.backend.model.entity.Inventory;
import com.example.backend.model.entity.Order;
import com.example.backend.service.InventoryService;
import com.example.backend.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Api(tags = " Inventory Receiving Management")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list/")
    @ApiOperation("Get all receiving records")
    public ResponseEntity<List<Inventory>> findAll() {
        List<Inventory> inventory =  inventoryService.getInventoryIn();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/add/")
    @ApiOperation("Admin stock-in")
    public ResponseEntity<Boolean> InventoryIn(@RequestBody Inventory inventory) {
        System.out.println("Received Inventory: " + inventory);
        Boolean success = inventoryService.InventoryIn(inventory);
        return ResponseEntity.ok(success);
    }
}
