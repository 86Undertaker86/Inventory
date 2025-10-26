package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.*;
import com.CourseWork.Inventory.Repository.*;
import com.CourseWork.Inventory.Service.StockMovementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final StockMovementService stockService;
    private final ItemRepository itemRepo;
    private final LocationRepository locationRepo;
    private final StockMovementRepository movementRepo;

    public ManagerController(StockMovementService stockService,
                             ItemRepository itemRepo,
                             LocationRepository locationRepo,
                             StockMovementRepository movementRepo) {
        this.stockService = stockService;
        this.itemRepo = itemRepo;
        this.locationRepo = locationRepo;
        this.movementRepo = movementRepo;
    }

    // Головна сторінка менеджера
    @GetMapping
    public String showManagerPage(Model model) {
        model.addAttribute("movement", new StockMovement());
        model.addAttribute("items", itemRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("movements", movementRepo.findAll());
        return "ManagerPage"; // templates/ManagerPage.html
    }

    // Реєстрація нового руху
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") StockMovement movement) {
        stockService.saveMovement(movement);
        return "redirect:/manager?success";
    }
}