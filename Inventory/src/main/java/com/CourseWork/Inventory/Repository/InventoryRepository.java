package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    /**
     * Finds a specific inventory record for a given item and location pair.
     * Returns an Optional containing the Inventory if found.
     */
    Optional<Inventory> findByItemAndLocation(Item item, Location location);

    /**
     * Finds any inventory record that occupies a specific location.
     * Returns an Optional containing the Inventory if found.
     */
    Optional<Inventory> findByLocation(Location location);
}