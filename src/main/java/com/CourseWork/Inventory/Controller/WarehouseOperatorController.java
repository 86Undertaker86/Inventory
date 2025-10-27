package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.MovementType;
import com.CourseWork.Inventory.Model.StockMovement;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import com.CourseWork.Inventory.Service.InventoryService;
import com.CourseWork.Inventory.Service.StockMovementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operator")
public class WarehouseOperatorController {

    private final StockMovementService stockMovementService;
    private final InventoryService inventoryService;
    private final ItemRepository itemRepo;
    private final LocationRepository locationRepo;

    public WarehouseOperatorController(StockMovementService stockMovementService,
                                       InventoryService inventoryService,
                                       ItemRepository itemRepo,
                                       LocationRepository locationRepo) {
        this.stockMovementService = stockMovementService;
        this.inventoryService = inventoryService;
        this.itemRepo = itemRepo;
        this.locationRepo = locationRepo;
    }

    // 📦 Головна сторінка комірника
    @GetMapping
    public String showOperatorPage(Model model) {
        model.addAttribute("movement", new StockMovement());
        model.addAttribute("items", itemRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("movements", stockMovementService.getAllMovements());
        return "OperatorPage";
    }

    // 🔁 Реєстрація руху товару (оприбуткування або списання)
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") StockMovement movement,
                              @RequestParam("item") Integer itemId,
                              @RequestParam(value = "fromLocation", required = false) Integer fromId,
                              @RequestParam(value = "toLocation", required = false) Integer toId,
                              @RequestParam(value = "location", required = false) Integer singleLocationId,
                              Model model) {

        var item = itemRepo.findById(itemId).orElse(null);

        try {
            if (movement.getMovement_type() == MovementType.IN || movement.getMovement_type() == MovementType.OUT) {
                var location = locationRepo.findById(singleLocationId).orElse(null);
                movement.setItem(item);
                movement.setLocation(location);

                if (movement.getMovement_type() == MovementType.IN) {
                    inventoryService.saveInventory(item, location, movement.getQuantity());
                } else {
                    inventoryService.saveInventory(item, location, -movement.getQuantity());
                }
            } else if (movement.getMovement_type() == MovementType.TRANSFER) {
                var fromLocation = locationRepo.findById(fromId).orElse(null);
                var toLocation = locationRepo.findById(toId).orElse(null);

                // Виконуємо переміщення
                inventoryService.transferInventory(item, fromLocation, toLocation, movement.getQuantity());

                // Можна записати у історію рухів (за бажанням)
                movement.setLocation(toLocation);
            }

            stockMovementService.saveMovement(movement);
            return "redirect:/operator?success";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movement", new StockMovement());
            model.addAttribute("items", itemRepo.findAll());
            model.addAttribute("locations", locationRepo.findAll());
            model.addAttribute("movements", stockMovementService.getAllMovements());
            return "OperatorPage";
        }
    }

    @GetMapping("/inventory")
    public String viewInventory(Model model) {
        model.addAttribute("inventories", inventoryService.getAllInventories());
        return "Inventory";
    }
}