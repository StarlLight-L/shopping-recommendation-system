package com.example.backend.service;

import com.example.backend.model.entity.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getInventoryIn();

    Boolean InventoryIn(Inventory inventory);
}
