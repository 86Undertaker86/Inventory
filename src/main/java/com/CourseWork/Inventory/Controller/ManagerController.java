package com.CourseWork.Inventory.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping
    public String managerPage() {
        return "ManagerPage";
    }
}