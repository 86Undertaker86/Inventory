package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/manager/inventory")
    public String showManagerInventory(Model model) {
        model.addAttribute("inventories", inventoryService.getAllInventories());
        model.addAttribute("role", "MANAGER");
        return "InventoryPage";
    }

    @GetMapping("/loader/inventory")
    public String showLoaderInventory(Model model) {
        model.addAttribute("inventories", inventoryService.getAllInventories());
        model.addAttribute("role", "LOADER");
        return "InventoryPage";
    }
}