package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Supplier;
import com.CourseWork.Inventory.Service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Перегляд усіх постачальників
    @GetMapping
    public String viewSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("newSupplier", new Supplier());
        return "SuppliersPage";
    }

    // Додавання нового постачальника
    @PostMapping("/add")
    public String addSupplier(@ModelAttribute("newSupplier") Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return "redirect:/manager/suppliers?success";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public Supplier getSupplierById(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }

    // Редагування (заповнення форми)
    @GetMapping("/edit/{id}")
    public String editSupplier(@PathVariable Integer id, Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("editSupplier", supplierService.getSupplierById(id));
        return "SuppliersPage";
    }

    // Оновлення
    @PostMapping("/update/{id}")
    public String updateSupplier(@PathVariable Integer id,
                                 @ModelAttribute("editSupplier") Supplier supplier) {
        supplier.setSupplier_id(id);
        supplierService.saveSupplier(supplier);
        return "redirect:/manager/suppliers?updated";
    }

    // Видалення
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return "redirect:/manager/suppliers?deleted";
    }
}