package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
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
        if (item == null || location == null) {
            throw new IllegalArgumentException("Помилка: товар або локація не знайдені.");
        }

        // Пошук існуючих записів
        Optional<Inventory> sameSlot = inventoryRepository.findByLocation(location);
        Optional<Inventory> exactMatch = inventoryRepository.findByItemAndLocation(item, location);

        // 🔒 Перевірка: чи не зайнята комірка іншим товаром
        if (sameSlot.isPresent() && exactMatch.isEmpty()) {
            Inventory existing = sameSlot.get();
            if (!existing.getItem().getItem_id().equals(item.getItem_id())) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: комірка %s-%s-%s вже містить товар '%s'. Неможливо додати '%s' в цю комірку.",
                        location.getRack(), location.getLevel(), location.getPosition(),
                        existing.getItem().getName(), item.getName()
                ));
            }
        }

        // 🔁 Якщо запис уже існує — оновлюємо кількість
        if (exactMatch.isPresent()) {
            Inventory inv = exactMatch.get();
            int current = inv.getQuantity();
            int newQuantity = current + deltaQuantity;

            if (deltaQuantity < 0 && Math.abs(deltaQuantity) > current) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: не можна списати %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                        Math.abs(deltaQuantity), item.getName(),
                        location.getRack(), location.getLevel(), location.getPosition(),
                        current
                ));
            }

            if (newQuantity <= 0) {
                inventoryRepository.delete(inv);
            } else {
                inv.setQuantity(newQuantity);
                inventoryRepository.save(inv);
            }
            return;
        }

        // 🆕 Якщо запису немає і deltaQuantity > 0 — створюємо
        if (deltaQuantity > 0) {
            Inventory inv = new Inventory();
            inv.setItem(item);
            inv.setLocation(location);
            inv.setQuantity(deltaQuantity);
            inventoryRepository.save(inv);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Помилка: не можна списати товар '%s' зі стелажа %s-%s-%s — він відсутній у комірці.",
                    item.getName(), location.getRack(), location.getLevel(), location.getPosition()
            ));
        }
    }

    /**
     * Переміщення між локаціями з перевіркою сумісності.
     * Якщо в цільовій локації є інший товар — переміщення заборонене.
     */
    @Transactional
    public void transferInventory(Item item, Location fromLocation, Location toLocation, int quantity) {
        if (item == null || fromLocation == null || toLocation == null) {
            throw new IllegalArgumentException("Помилка: товар або локації не знайдені.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Помилка: кількість для переміщення повинна бути більшою за нуль.");
        }

        // 1️⃣ Перевірка джерела (fromLocation)
        Inventory fromInv = inventoryRepository.findByItemAndLocation(item, fromLocation)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Помилка: у комірці %s-%s-%s немає товару '%s'.",
                        fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition(),
                        item.getName()
                )));

        if (fromInv.getQuantity() < quantity) {
            throw new IllegalArgumentException(String.format(
                    "Помилка: не можна перемістити %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                    quantity, item.getName(),
                    fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition(),
                    fromInv.getQuantity()
            ));
        }

        // 2️⃣ Перевірка цільової комірки (toLocation)
        Optional<Inventory> toSlot = inventoryRepository.findByLocation(toLocation);

        if (toSlot.isPresent()) {
            Inventory toInv = toSlot.get();
            if (!toInv.getItem().getItem_id().equals(item.getItem_id())) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: комірка %s-%s-%s вже містить інший товар ('%s'). Неможливо перемістити '%s'.",
                        toLocation.getRack(), toLocation.getLevel(), toLocation.getPosition(),
                        toInv.getItem().getName(), item.getName()
                ));
            }
        }

        // 3️⃣ Виконуємо переміщення атомарно
        saveInventory(item, fromLocation, -quantity);
        saveInventory(item, toLocation, quantity);
    }
}