package com.CourseWork.Inventory.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Головна сторінка адміністратора
    @GetMapping
    public String adminPage() {
        return "AdminPage";
    }
}