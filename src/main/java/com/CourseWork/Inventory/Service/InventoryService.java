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
        if (item == null || location == null) {
            throw new IllegalArgumentException("Помилка: товар або локація не знайдені.");
        }

        // Знайдемо конкретний запис для цієї пари item+location (якщо є)
        Optional<Inventory> optExact = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(location.getLocation_id()))
                .findFirst();

        // Знайдемо будь-який запис, який займає цю локацію (незалежно від товару)
        Optional<Inventory> optByLocation = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getLocation().getLocation_id().equals(location.getLocation_id()))
                .findFirst();

        // Якщо на локації є інший товар (і це не той самий item)
        if (optByLocation.isPresent() && optByLocation.get().getItem() != null
                && !optByLocation.get().getItem().getItem_id().equals(item.getItem_id())) {

            // Дозволяємо доступ тільки якщо це саме збільшення для того самого товару (optExact present)
            // але оскільки optByLocation містить інший товар — навіть IN заборонено
            throw new IllegalArgumentException(String.format(
                    "Помилка: на локації %s-%s-%s вже знаходиться товар '%s'. Неможливо додати '%s' в ту саму комірку.",
                    location.getRack(), location.getLevel(), location.getPosition(),
                    optByLocation.get().getItem().getName(),
                    item.getName()
            ));
        }

        // Тепер опрацьовуємо точний запис (якщо існує) або створюємо новий (тільки якщо deltaQuantity > 0)
        if (optExact.isPresent()) {
            Inventory inventory = optExact.get();
            int current = inventory.getQuantity();
            int newQuantity = current + deltaQuantity;

            // Якщо намагаємось списати більше, ніж є
            if (deltaQuantity < 0 && Math.abs(deltaQuantity) > current) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: не можна списати %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                        Math.abs(deltaQuantity), item.getName(),
                        location.getRack(), location.getLevel(), location.getPosition(),
                        current));
            }

            // Якщо кількість стає <= 0 — видаляємо запис
            if (newQuantity <= 0) {
                inventoryRepository.delete(inventory);
                return;
            }

            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        } else {
            // optExact відсутній — запису для цього item+location немає
            if (deltaQuantity > 0) {
                // Ми вже переконались вище, що на локації немає іншого товару (optByLocation absent)
                Inventory inventory = new Inventory();
                inventory.setItem(item);
                inventory.setLocation(location);
                inventory.setQuantity(deltaQuantity);
                inventoryRepository.save(inventory);
            } else {
                // OUT при відсутньому записі — помилка
                throw new IllegalArgumentException(String.format(
                        "Помилка: не можна списати товар '%s' зі стелажа %s-%s-%s — він відсутній у комірці.",
                        item.getName(), location.getRack(), location.getLevel(), location.getPosition()));
            }
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

        // Перевірка наявності у джерелі
        Optional<Inventory> optFrom = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getItem().getItem_id().equals(item.getItem_id())
                        && inv.getLocation().getLocation_id().equals(fromLocation.getLocation_id()))
                .findFirst();

        if (optFrom.isEmpty() || optFrom.get().getQuantity() < quantity) {
            throw new IllegalArgumentException(String.format(
                    "Помилка: не можна перемістити %d одиниць товару '%s' зі стелажа %s-%s-%s — доступно лише %d.",
                    quantity, item.getName(),
                    fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition(),
                    optFrom.map(Inventory::getQuantity).orElse(0)));
        }

        // Перевірка цільової локації: чи є там інший товар?
        Optional<Inventory> optToByLocation = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getLocation().getLocation_id().equals(toLocation.getLocation_id()))
                .findFirst();

        if (optToByLocation.isPresent()) {
            Inventory toInv = optToByLocation.get();
            if (!toInv.getItem().getItem_id().equals(item.getItem_id())) {
                throw new IllegalArgumentException(String.format(
                        "Помилка: комірка %s-%s-%s уже містить інший товар ('%s'). Неможливо перемістити '%s'.",
                        toLocation.getRack(), toLocation.getLevel(), toLocation.getPosition(),
                        toInv.getItem().getName(), item.getName()));
            }
        }

        // Виконуємо переміщення: списуємо з from, додаємо до to
        saveInventory(item, fromLocation, -quantity);
        saveInventory(item, toLocation, quantity);
    }
}