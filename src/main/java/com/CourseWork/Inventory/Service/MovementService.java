package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Movement;
import com.CourseWork.Inventory.Repository.MovementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovementService {
    private final MovementRepository movementRepository;

    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public void saveMovement(Movement movement) {
        movementRepository.save(movement);
    }

    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }
}