package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.LoginRequest;
import com.CourseWork.Inventory.Service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Головна сторінка адміністратора
    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "LoginPage";
    }

    // Відкриття форми редагування
    @PostMapping
    public String processLogin(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model) {
        String result = loginService.validateLogin(loginRequest);

        if (result.equals("success")) {
            switch (loginRequest.getRoleUser()) {
                case ADMIN -> { return "redirect:/admin"; }
                case MANAGER -> { return "redirect:/manager"; }
                case LOADER -> { return "redirect:/loader"; }
                default -> { return "redirect:/login?error=Невідома роль"; }
            }
        } else {
            model.addAttribute("errorMessage", result);
            model.addAttribute("loginRequest", loginRequest);
            model.addAttribute("roles", loginService.getAllRoles());
            return "LoginPage";
        }
    }
}