package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
}