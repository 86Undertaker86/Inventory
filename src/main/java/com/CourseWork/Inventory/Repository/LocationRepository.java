package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>{
}