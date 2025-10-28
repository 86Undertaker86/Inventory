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

    // Список товарів
    @GetMapping
    public String viewItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("newItem", new Item());
        model.addAttribute("suppliers", supplierRepository.findAll()); // 👈 додай це
        return "ItemPage";
    }

    // Додавання нового товару
    @PostMapping("/add")
    public String addItem(@ModelAttribute("newItem") Item item,
                          @RequestParam(value = "supplier", required = false) Integer supplierId) {
        if (supplierId != null) {
            item.setSupplier(supplierRepository.findById(supplierId).orElse(null));
        }
        itemService.saveItem(item);
        return "redirect:/manager/items?success";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public Item getItemById(@PathVariable("id") Integer id) {
        return itemService.getItemById(id);
    }

    // Редагування (заповнення форми)
    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable("id") Integer id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("editItem", item);
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("suppliers", supplierRepository.findAll()); // Додаємо список постачальників
        return "ItemPage";
    }

    // Збереження змін
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

    // Видалення товару
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Integer id) {
        itemService.deleteItem(id);
        return "redirect:/manager/items?deleted";
    }
}