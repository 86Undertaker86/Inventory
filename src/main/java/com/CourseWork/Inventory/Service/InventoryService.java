package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Оновлює кількість товару на локації:
     *  - додає при IN,
     *  - віднімає при OUT,
     *  - видаляє, якщо кількість стає 0,
     *  - кидає помилку, якщо OUT > наявної кількості.
     */
    @Transactional
    public void saveInventory(Item item, Location location, int deltaQuantity) {
        Inventory inventory = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(location.getLocation_id()))
                .findFirst()
                .orElse(null);

        // Якщо запис існує
        if (inventory != null) {
            int current = inventory.getQuantity();
            int newQuantity = current + deltaQuantity;

            // ❌ Якщо намагаємось списати більше, ніж є
            if (deltaQuantity < 0 && Math.abs(deltaQuantity) > current) {
                throw new IllegalArgumentException(
                        String.format("Помилка: не можна списати %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                                Math.abs(deltaQuantity),
                                item.getName(),
                                location.getRack(), location.getLevel(), location.getPosition(),
                                current)
                );
            }

            // 🗑️ Якщо кількість стає 0 — видаляємо запис
            if (newQuantity <= 0) {
                inventoryRepository.delete(inventory);
                return;
            }

            // 🔄 Оновлення кількості
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        }
        // Якщо запису нема, але це IN — створюємо
        else if (deltaQuantity > 0) {
            inventory = new Inventory();
            inventory.setItem(item);
            inventory.setLocation(location);
            inventory.setQuantity(deltaQuantity);
            inventoryRepository.save(inventory);
        }
        // Якщо запису нема, а це OUT — помилка
        else {
            throw new IllegalArgumentException(
                    String.format("Помилка: не можна списати товар '%s' зі стелажа %s-%s-%s — він відсутній у комірці.",
                            item.getName(),
                            location.getRack(), location.getLevel(), location.getPosition())
            );
        }
    }
}