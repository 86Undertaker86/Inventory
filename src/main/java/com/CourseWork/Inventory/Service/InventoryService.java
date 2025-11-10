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
     * Updates the quantity of an item at a specific location.
     * - Adds stock for IN operations
     * - Subtracts stock for OUT operations
     * - Deletes inventory if quantity becomes 0
     * - Throws an exception if OUT quantity exceeds available stock
     */
    @Transactional
    public void saveInventory(Item item, Location location, int deltaQuantity) {
        if (item == null || location == null) {
            throw new IllegalArgumentException("Error: Item or location not found.");
        }

        // Find existing inventory records
        Optional<Inventory> sameSlot = inventoryRepository.findByLocation(location);
        Optional<Inventory> exactMatch = inventoryRepository.findByItemAndLocation(item, location);

        // Check if the slot is already occupied by a different item
        if (sameSlot.isPresent() && exactMatch.isEmpty()) {
            Inventory existing = sameSlot.get();
            if (!existing.getItem().getItem_id().equals(item.getItem_id())) {
                throw new IllegalArgumentException(String.format(
                        "Error: Slot %s-%s-%s already contains item '%s'. Cannot add '%s'.",
                        location.getRack(), location.getLevel(), location.getPosition(),
                        existing.getItem().getName(), item.getName()
                ));
            }
        }

        // Update quantity if inventory record already exists
        if (exactMatch.isPresent()) {
            Inventory inv = exactMatch.get();
            int current = inv.getQuantity();
            int newQuantity = current + deltaQuantity;

            if (deltaQuantity < 0 && Math.abs(deltaQuantity) > current) {
                throw new IllegalArgumentException(String.format(
                        "Error: Cannot remove %d units of '%s' from %s-%s-%s — only %d available.",
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

        // Create a new inventory record if none exists and deltaQuantity > 0
        if (deltaQuantity > 0) {
            Inventory inv = new Inventory();
            inv.setItem(item);
            inv.setLocation(location);
            inv.setQuantity(deltaQuantity);
            inventoryRepository.save(inv);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Error: Cannot remove '%s' from %s-%s-%s — item not found in slot.",
                    item.getName(), location.getRack(), location.getLevel(), location.getPosition()
            ));
        }
    }

    /**
     * Transfers a quantity of an item from one location to another.
     * Ensures the target slot either is empty or contains the same item.
     * Performs the operation atomically using saveInventory().
     */
    @Transactional
    public void transferInventory(Item item, Location fromLocation, Location toLocation, int quantity) {
        if (item == null || fromLocation == null || toLocation == null) {
            throw new IllegalArgumentException("Error: Item or locations not found.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Error: Quantity to transfer must be greater than zero.");
        }

        // Check source location
        Inventory fromInv = inventoryRepository.findByItemAndLocation(item, fromLocation)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Error: Item '%s' not found in slot %s-%s-%s.",
                        item.getName(), fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition()
                )));

        if (fromInv.getQuantity() < quantity) {
            throw new IllegalArgumentException(String.format(
                    "Error: Cannot transfer %d units of '%s' from %s-%s-%s — only %d available.",
                    quantity, item.getName(),
                    fromLocation.getRack(), fromLocation.getLevel(), fromLocation.getPosition(),
                    fromInv.getQuantity()
            ));
        }

        // Check target location
        Optional<Inventory> toSlot = inventoryRepository.findByLocation(toLocation);
        if (toSlot.isPresent()) {
            Inventory toInv = toSlot.get();
            if (!toInv.getItem().getItem_id().equals(item.getItem_id())) {
                throw new IllegalArgumentException(String.format(
                        "Error: Slot %s-%s-%s contains a different item ('%s'). Cannot transfer '%s'.",
                        toLocation.getRack(), toLocation.getLevel(), toLocation.getPosition(),
                        toInv.getItem().getName(), item.getName()
                ));
            }
        }

        // Perform transfer atomically
        saveInventory(item, fromLocation, -quantity);
        saveInventory(item, toLocation, quantity);
    }
}