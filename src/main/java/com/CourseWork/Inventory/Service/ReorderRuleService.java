package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Repository.InventoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReorderRuleService {

    private final InventoryRepository inventoryRepository;

    public ReorderRuleService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /** Отримати всі позиції, де кількість менша за мінімальний рівень */
    public List<Inventory> getLowStockInventories() {
        return inventoryRepository.findAll();
    }
}