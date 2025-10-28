package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Service.InventoryService;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // 🧾 Перегляд залишків
    @GetMapping
    public String showInventoryPage(Model model) {
        model.addAttribute("inventories", inventoryService.getAllInventories());
        return "InventoryPage";
    }
}
