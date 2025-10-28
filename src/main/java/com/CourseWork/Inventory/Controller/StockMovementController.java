package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Movement;
import com.CourseWork.Inventory.Service.WarehouseOperatorService;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import com.CourseWork.Inventory.Service.MovementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StockMovementController {

    private final WarehouseOperatorService operatorService;
    private final ItemRepository itemRepo;
    private final LocationRepository locationRepo;
    private final MovementService movementService;

    public StockMovementController(WarehouseOperatorService operatorService,
                                       ItemRepository itemRepo,
                                       LocationRepository locationRepo,
                                       MovementService movementService) {
        this.operatorService = operatorService;
        this.itemRepo = itemRepo;
        this.locationRepo = locationRepo;
        this.movementService = movementService;
    }

    // 📦 Головна сторінка комірника
    @GetMapping("/manager/movements")
    public String showManagerMovements(Model model) {
        model.addAttribute("role", "MANAGER");
        model.addAttribute("movements", movementService.getAllMovements());
        return "MovementPage";
    }

    @GetMapping("/loader/movements")
    public String showOperatorMovements(Model model) {
        model.addAttribute("role", "LOADER");
        model.addAttribute("movement", new Movement());
        model.addAttribute("items", itemRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("movements", movementService.getAllMovements());
        return "MovementPage";
    }

    // 🔁 Реєстрація руху товару
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") Movement movement,
                              @RequestParam("item") Integer itemId,
                              @RequestParam(value = "fromLocation", required = false) Integer fromId,
                              @RequestParam(value = "toLocation", required = false) Integer toId,
                              @RequestParam(value = "location", required = false) Integer singleLocationId,
                              Model model) {
        try {
            operatorService.processMovement(movement, itemId, fromId, toId, singleLocationId);
            return "redirect:/loader?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movement", new Movement());
            model.addAttribute("items", itemRepo.findAll());
            model.addAttribute("locations", locationRepo.findAll());
            model.addAttribute("movements", movementService.getAllMovements());
            return "MovementPage";
        }
    }
}