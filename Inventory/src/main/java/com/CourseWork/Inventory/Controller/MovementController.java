package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Movement;
import com.CourseWork.Inventory.Repository.ItemRepository;
import com.CourseWork.Inventory.Repository.LocationRepository;
import com.CourseWork.Inventory.Service.MovementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovementController {
    private final ItemRepository itemRepo;
    private final LocationRepository locationRepo;
    private final MovementService movementService;

    public MovementController(ItemRepository itemRepo,
                              LocationRepository locationRepo,
                              MovementService movementService) {
        this.itemRepo = itemRepo;
        this.locationRepo = locationRepo;
        this.movementService = movementService;
    }

    /**
     * Displays the movement history page for the manager.
     * Only shows the list of all recorded movements (no form for adding new ones).
     */
    @GetMapping("/manager/movements")
    public String showManagerMovements(Model model) {
        model.addAttribute("role", "MANAGER");
        model.addAttribute("movements", movementService.getAllMovements());
        return "MovementPage";
    }

    /**
     * Displays the movement page for the loader (warehouse operator).
     * Includes the form for adding a new movement and lists of items and locations.
     */
    @GetMapping("/loader/movements")
    public String showOperatorMovements(Model model) {
        model.addAttribute("role", "LOADER");
        model.addAttribute("movement", new Movement());
        model.addAttribute("items", itemRepo.findAll());
        model.addAttribute("locations", locationRepo.findAll());
        model.addAttribute("movements", movementService.getAllMovements());
        return "MovementPage";
    }

    /**
     * Handles the registration of a new item movement.
     * Depending on user input, this may represent:
     * - movement from one location to another,
     * - adding an item to inventory,
     * - or removing an item from a location.
     *
     * If processing fails (e.g., invalid data or movement logic),
     * the same page is reloaded with an error message.
     */
    @PostMapping("/add")
    public String addMovement(@ModelAttribute("movement") Movement movement,
                              @RequestParam("item") Integer itemId,
                              @RequestParam(value = "fromLocation", required = false) Integer fromId,
                              @RequestParam(value = "toLocation", required = false) Integer toId,
                              @RequestParam(value = "location", required = false) Integer singleLocationId,
                              Model model) {
        try {
            movementService.processMovement(movement, itemId, fromId, toId, singleLocationId);
            return "redirect:/loader?success";
        } catch (IllegalArgumentException e) {
            // If an error occurs, reload the page with error details
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movement", new Movement());
            model.addAttribute("items", itemRepo.findAll());
            model.addAttribute("locations", locationRepo.findAll());
            model.addAttribute("movements", movementService.getAllMovements());
            model.addAttribute("role", "LOADER");
            return "MovementPage";
        }
    }
}