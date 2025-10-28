package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.StockMovement;
import com.CourseWork.Inventory.Service.WarehouseOperatorService;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import com.CourseWork.Inventory.Service.StockMovementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operator")
public class WarehouseOperatorController {

    private final WarehouseOperatorService operatorService;
    private final ItemRepository itemRepo;
    private final LocationRepository locationRepo;
    private final StockMovementService stockMovementService;

    public WarehouseOperatorController(WarehouseOperatorService operatorService,
                                       ItemRepository itemRepo,
                                       LocationRepository locationRepo,
                                       StockMovementService stockMovementService) {
        this.operatorService = operatorService;
        this.itemRepo = itemRepo;
        this.locationRepo = locationRepo;
        this.stockMovementService = stockMovementService;
    }

    // 📦 Головна сторінка комірника
    @GetMapping
    public String showOperatorMovements(Model model) {
        model.addAttribute("role", "OPERATOR");
        model.addAttribute("movement", new StockMovement());
        model.addAttribute("items", itemRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("movements", stockMovementService.getAllMovements());
        return "MovementsPage";
    }

    // 🔁 Реєстрація руху товару
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") StockMovement movement,
                              @RequestParam("item") Integer itemId,
                              @RequestParam(value = "fromLocation", required = false) Integer fromId,
                              @RequestParam(value = "toLocation", required = false) Integer toId,
                              @RequestParam(value = "location", required = false) Integer singleLocationId,
                              Model model) {
        try {
            operatorService.processMovement(movement, itemId, fromId, toId, singleLocationId);
            return "redirect:/operator?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movement", new StockMovement());
            model.addAttribute("items", itemRepo.findAll());
            model.addAttribute("locations", locationRepo.findAll());
            model.addAttribute("movements", stockMovementService.getAllMovements());
            return "MovementsPage";
        }
    }
}