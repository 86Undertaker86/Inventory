package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Service.ReorderRuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager/reorder")
public class ReorderRuleController {

    private final ReorderRuleService reorderRuleService;

    public ReorderRuleController(ReorderRuleService reorderRuleService) {
        this.reorderRuleService = reorderRuleService;
    }

    /**
     * Displays the reorder rule page for the manager.
     * Fetches all inventory items that are below their minimum stock level.
     */
    @GetMapping()
    public String showReorderRule(Model model) {
        model.addAttribute("reorders", reorderRuleService.getLowStockInventories());
        return "ReorderPage";
    }

    /**
     * Simulates sending a reorder notification to the supplier.
     * In a full implementation, this could trigger an email or API request.
     */
    @GetMapping("/push")
    public String pushReorder() {
        System.out.println("Notification sent to the supplier");
        return "redirect:/manager/reorder?success";
    }
}