package com.CourseWork.Inventory.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    // Головна сторінка менеджера
    @GetMapping
    public String showManagerPage() {
        return "ManagerPage";
    }
}