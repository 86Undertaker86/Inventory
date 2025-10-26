package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.StockMovement;
import com.CourseWork.Inventory.Repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WarehouseOperatorService {

    private final StockMovementRepository movementRepo;

    public WarehouseOperatorService(StockMovementRepository movementRepo) {
        this.movementRepo = movementRepo;
    }

    public void recordMovement(StockMovement movement) {
        movementRepo.save(movement);
    }

    public List<StockMovement> getAllMovements() {
        return movementRepo.findAll();
    }
}