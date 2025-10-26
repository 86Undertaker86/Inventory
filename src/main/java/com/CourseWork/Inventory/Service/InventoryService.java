package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Integer id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public void deleteInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }

    // 🔧 Оновлення залишків з урахуванням item і location
    public void updateQuantity(Item item, Location location, int deltaQuantity) {
        Inventory inventory = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(location.getLocation_id()))
                .findFirst()
                .orElse(null);

        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + deltaQuantity);
        } else {
            inventory = new Inventory();
            inventory.setItem(item);
            inventory.setLocation(location);
            inventory.setQuantity(deltaQuantity);
        }

        inventoryRepository.save(inventory);
    }
}