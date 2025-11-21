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

    /**
     * Displays the list of all suppliers.
     * Also prepares an empty Supplier object for the add form.
     */
    @GetMapping
    public String viewSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("newSupplier", new Supplier());
        return "SupplierPage";
    }

    /**
     * Handles the creation of a new supplier and saves it to the database.
     */
    @PostMapping("/add")
    public String addSupplier(@ModelAttribute("newSupplier") Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return "redirect:/manager/suppliers?success";
    }

    /**
     * Returns supplier data by ID in JSON format (useful for AJAX requests).
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public Supplier getSupplierById(@PathVariable Integer id) {
        return supplierService.getSupplierById(id);
    }

    /**
     * Loads supplier data into the edit form.
     * Also provides the list of all suppliers for display on the same page.
     */
    @GetMapping("/edit/{id}")
    public String editSupplier(@PathVariable Integer id, Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("editSupplier", supplierService.getSupplierById(id));
        return "SupplierPage";
    }

    /**
     * Updates an existing supplier’s information.
     */
    @PostMapping("/update/{id}")
    public String updateSupplier(@PathVariable Integer id,
                                 @ModelAttribute("editSupplier") Supplier supplier) {
        supplier.setSupplier_id(id);
        supplierService.saveSupplier(supplier);
        return "redirect:/manager/suppliers?updated";
    }

    /**
     * Deletes a supplier by ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return "redirect:/manager/suppliers?deleted";
    }
}