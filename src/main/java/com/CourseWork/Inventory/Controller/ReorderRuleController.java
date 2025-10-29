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

    @GetMapping()
    public String showReorderRule(Model model) {
        model.addAttribute("reorders", reorderRuleService.getLowStockInventories());
        return "ReorderPage";
    }
}
