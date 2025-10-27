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
    // 🔁 Оприбуткування / списання / переміщення
    @Transactional
    public void saveInventory(Item item, Location location, int deltaQuantity) {
        Inventory inventory = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(location.getLocation_id()))
                .findFirst()
                .orElse(null);

        if (inventory != null) {
            int current = inventory.getQuantity();
            int newQuantity = current + deltaQuantity;

            if (deltaQuantity < 0 && Math.abs(deltaQuantity) > current) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: не можна списати %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                        Math.abs(deltaQuantity), item.getName(),
                        location.getRack(), location.getLevel(), location.getPosition(),
                        current));
            }

            if (newQuantity <= 0) {
                inventoryRepository.delete(inventory);
                return;
            }

            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        } else if (deltaQuantity > 0) {
            inventory = new Inventory();
            inventory.setItem(item);
            inventory.setLocation(location);
            inventory.setQuantity(deltaQuantity);
            inventoryRepository.save(inventory);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Помилка: не можна списати товар '%s' зі стелажа %s-%s-%s — він відсутній у комірці.",
                    item.getName(),
                    location.getRack(), location.getLevel(), location.getPosition()));
        }
    }

    // 🚚 Переміщення між двома локаціями
    @Transactional
    public void transferInventory(Item item, Location fromLocation, Location toLocation, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Помилка: кількість для переміщення повинна бути більшою за нуль.");
        }

        // 1️⃣ Отримуємо залишок у джерелі
        Inventory fromInv = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(fromLocation.getLocation_id()))
                .findFirst()
                .orElse(null);

        if (fromInv == null || fromInv.getQuantity() < quantity) {
            throw new IllegalArgumentException(String.format(
                    "Помилка: не можна перемістити %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                    quantity, item.getName(),
                    fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition(),
                    fromInv == null ? 0 : fromInv.getQuantity()));
        }

        // 2️⃣ Перевіряємо цільову комірку
        Inventory toInv = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getLocation().getLocation_id().equals(toLocation.getLocation_id()))
                .findFirst()
                .orElse(null);

        // Якщо цільова комірка вже зайнята іншим товаром
        if (toInv != null && !toInv.getItem().getItem_id().equals(item.getItem_id())) {
            throw new IllegalArgumentException(String.format(
                    "Помилка: комірка %s-%s-%s вже містить інший товар ('%s'). " +
                            "Неможливо перемістити '%s'.",
                    toLocation.getRack(), toLocation.getLevel(), toLocation.getPosition(),
                    toInv.getItem().getName(), item.getName()));
        }

        // 3️⃣ Віднімаємо у джерелі
        saveInventory(item, fromLocation, -quantity);

        // 4️⃣ Додаємо в ціль
        saveInventory(item, toLocation, quantity);
    }
}