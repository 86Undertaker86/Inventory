package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {
}