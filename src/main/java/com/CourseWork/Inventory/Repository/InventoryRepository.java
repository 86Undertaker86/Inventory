package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.Inventory;
import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // 🔹 Шукає конкретний запис для пари товар + локація
    Optional<Inventory> findByItemAndLocation(Item item, Location location);

    // 🔹 Шукає будь-який запис, який займає конкретну локацію
    Optional<Inventory> findByLocation(Location location);
}