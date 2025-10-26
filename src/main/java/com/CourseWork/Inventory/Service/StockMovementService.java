package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.StockMovement;
import com.CourseWork.Inventory.Repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockMovementService {
    private final StockMovementRepository repo;

    public StockMovementService(StockMovementRepository repo) {
        this.repo = repo;
    }

    public void saveMovement(StockMovement movement) {
        repo.save(movement);
    }

    public List<StockMovement> getAllMovements() {
        return repo.findAll();
    }
}