package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Service.ItemService;
import com.CourseWork.Inventory.Repository.SupplierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager/items")
public class ItemController {

    private final ItemService itemService;
    private final SupplierRepository supplierRepository;

    public ItemController(ItemService itemService, SupplierRepository supplierRepository) {
        this.itemService = itemService;
        this.supplierRepository = supplierRepository;
    }

    /**
     * Displays the list of all items.
     * Adds a new empty Item object for the creation form and
     * loads all suppliers for dropdown selection.
     */
    @GetMapping
    public String viewItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("newItem", new Item());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "ItemPage";
    }

    /**
     * Handles the creation of a new item.
     * Associates the item with a supplier (if provided) and saves it.
     */
    @PostMapping("/add")
    public String addItem(@ModelAttribute("newItem") Item item,
                          @RequestParam(value = "supplier", required = false) Integer supplierId) {
        if (supplierId != null) {
            item.setSupplier(supplierRepository.findById(supplierId).orElse(null));
        }
        itemService.saveItem(item);
        return "redirect:/manager/items?success";
    }

    /**
     * Returns an item by its ID in JSON format (used for AJAX requests).
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public Item getItemById(@PathVariable("id") Integer id) {
        return itemService.getItemById(id);
    }

    /**
     * Loads the item into the form for editing.
     * Also adds all items and suppliers for page rendering.
     */
    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable("id") Integer id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("editItem", item);
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "ItemPage";
    }

    /**
     * Saves changes to an existing item.
     * Updates supplier relation if provided.
     */
    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable("id") Integer id,
                             @ModelAttribute("editItem") Item updatedItem,
                             @RequestParam(value = "supplier", required = false) Integer supplierId) {
        if (supplierId != null) {
            updatedItem.setSupplier(supplierRepository.findById(supplierId).orElse(null));
        }
        updatedItem.setItem_id(id);
        itemService.saveItem(updatedItem);
        return "redirect:/manager/items?updated";
    }

    /**
     * Deletes an item by its ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Integer id) {
        itemService.deleteItem(id);
        return "redirect:/manager/items?deleted";
    }
}