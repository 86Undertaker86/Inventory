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

    public ManagerController(StockMovementService stockService) {
        this.stockService = stockService;
    }

    // Головна сторінка менеджера
    @GetMapping
    public String showMovementsForManager(Model model) {
        model.addAttribute("role", "MANAGER");
        model.addAttribute("movements", stockService.getAllMovements());
        return "MovementsPage";
    }

    // Реєстрація нового руху
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") StockMovement movement) {
        stockService.saveMovement(movement);
        return "redirect:/manager?success";
    }
}