package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Model.MovementType;
import com.CourseWork.Inventory.Model.Movement;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoaderService {

    private final MovementService movementService;
    private final InventoryService inventoryService;
    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;

    public LoaderService(MovementService movementService,
                         InventoryService inventoryService,
                         ItemRepository itemRepository,
                         LocationRepository locationRepository) {
        this.movementService = movementService;
        this.inventoryService = inventoryService;
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Основна бізнес-логіка для обробки рухів товарів
     */
    @Transactional
    public void processMovement(Movement movement,
                                Integer itemId,
                                Integer fromId,
                                Integer toId,
                                Integer singleLocationId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Помилка: товар не знайдено."));

        MovementType type = movement.getMovement_type();

        switch (type) {
            case IN -> {
                Location location = locationRepository.findById(singleLocationId)
                        .orElseThrow(() -> new IllegalArgumentException("Помилка: локацію не знайдено."));
                movement.setItem(item);
                movement.setLocation(location);
                inventoryService.saveInventory(item, location, movement.getQuantity());
            }

            case OUT -> {
                Location location = locationRepository.findById(singleLocationId)
                        .orElseThrow(() -> new IllegalArgumentException("Помилка: локацію не знайдено."));
                movement.setItem(item);
                movement.setLocation(location);
                inventoryService.saveInventory(item, location, -movement.getQuantity());
            }

            case TRANSFER -> {
                Location fromLocation = locationRepository.findById(fromId)
                        .orElseThrow(() -> new IllegalArgumentException("Помилка: вихідну локацію не знайдено."));
                Location toLocation = locationRepository.findById(toId)
                        .orElseThrow(() -> new IllegalArgumentException("Помилка: цільову локацію не знайдено."));
                inventoryService.transferInventory(item, fromLocation, toLocation, movement.getQuantity());
                movement.setItem(item);
                movement.setLocation(toLocation);
            }

            default -> throw new IllegalArgumentException("Невідомий тип руху товару.");
        }

        // ✅ Зберігаємо рух тільки якщо всі операції пройшли успішно
        movementService.saveMovement(movement);
    }
}