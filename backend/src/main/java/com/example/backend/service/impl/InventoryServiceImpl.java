package com.example.backend.service.impl;

import com.example.backend.mapper.InventoryMapper;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.entity.Inventory;
import com.example.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Inventory> getInventoryIn() {
        return inventoryMapper.getInventoryInList();
    }

    @Override
    public Boolean InventoryIn(Inventory inventory) {
        inventoryMapper.InventoryAdd(inventory);
        productMapper.updateStock(inventory.getProduct_id(),inventory.getQuantity());
        return true;
    }
}
