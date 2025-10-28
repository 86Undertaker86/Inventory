package com.CourseWork.Inventory.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loader")
public class LoaderController {

    // 📦 Головна сторінка комірника
    @GetMapping
    public String loaderPage() {
        return "LoaderPage";
    }
}