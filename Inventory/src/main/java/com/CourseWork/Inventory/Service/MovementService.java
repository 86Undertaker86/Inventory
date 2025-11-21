package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Model.Movement;
import com.CourseWork.Inventory.Model.MovementType;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import com.CourseWork.Inventory.Repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovementService {
    private final MovementRepository movementRepository;
    private final ItemRepository itemRepository;
    private final LocationRepository locationRepository;
    private final InventoryService inventoryService;

    public MovementService(MovementRepository movementRepository,
                           ItemRepository itemRepository,
                           LocationRepository locationRepository,
                           InventoryService inventoryService) {
        this.movementRepository = movementRepository;
        this.itemRepository = itemRepository;
        this.locationRepository = locationRepository;
        this.inventoryService = inventoryService;
    }

    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    /**
     * Processes a movement of items according to its type:
     * - IN: Adds stock to a single location
     * - OUT: Removes stock from a single location
     * - TRANSFER: Moves stock from one location to another
     *
     * The operation is transactional: the movement is only saved if all steps succeed.
     *
     * @param movement The movement object containing quantity and type
     * @param itemId ID of the item being moved
     * @param fromId Source location ID (for TRANSFER)
     * @param toId Target location ID (for TRANSFER)
     * @param singleLocationId Location ID for IN or OUT operations
     */
    @Transactional
    public void processMovement(Movement movement,
                                Integer itemId,
                                Integer fromId,
                                Integer toId,
                                Integer singleLocationId) {

        // Fetch the item
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Error: Item not found."));

        MovementType type = movement.getMovement_type();

        switch (type) {
            case IN -> {
                // Add stock to a single location
                Location location = locationRepository.findById(singleLocationId)
                        .orElseThrow(() -> new IllegalArgumentException("Error: Location not found."));
                movement.setItem(item);
                movement.setLocation(location);
                inventoryService.saveInventory(item, location, movement.getQuantity());
            }

            case OUT -> {
                // Remove stock from a single location
                Location location = locationRepository.findById(singleLocationId)
                        .orElseThrow(() -> new IllegalArgumentException("Error: Location not found."));
                movement.setItem(item);
                movement.setLocation(location);
                inventoryService.saveInventory(item, location, -movement.getQuantity());
            }

            case TRANSFER -> {
                // Transfer stock between two locations
                Location fromLocation = locationRepository.findById(fromId)
                        .orElseThrow(() -> new IllegalArgumentException("Error: Source location not found."));
                Location toLocation = locationRepository.findById(toId)
                        .orElseThrow(() -> new IllegalArgumentException("Error: Target location not found."));
                inventoryService.transferInventory(item, fromLocation, toLocation, movement.getQuantity());
                movement.setItem(item);
                movement.setLocation(toLocation);
            }

            default -> throw new IllegalArgumentException("Unknown movement type.");
        }

        // Save the movement record only if all operations succeed
        movementRepository.save(movement);
    }
}