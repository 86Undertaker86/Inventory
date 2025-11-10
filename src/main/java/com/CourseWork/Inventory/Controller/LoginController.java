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

    /**
     * Displays the login page and initializes an empty LoginRequest object.
     */
    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "LoginPage";
    }

    /**
     * Processes login form submission.
     * Validates credentials and redirects users to their respective dashboards
     * based on their role (ADMIN, MANAGER, or LOADER).
     * If validation fails, returns to the login page with an error message.
     */
    @PostMapping
    public String processLogin(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model) {
        String result = loginService.validateLogin(loginRequest);

        if (result.equals("success")) {
            // Redirect user based on their role
            switch (loginRequest.getRoleUser()) {
                case ADMIN -> { return "redirect:/admin"; }
                case MANAGER -> { return "redirect:/manager"; }
                case LOADER -> { return "redirect:/loader"; }
                default -> { return "redirect:/login?error=UnknownRole"; }
            }
        } else {
            // If login fails, show error and reload login page
            model.addAttribute("errorMessage", result);
            model.addAttribute("loginRequest", loginRequest);
            model.addAttribute("roles", loginService.getAllRoles());
            return "LoginPage";
        }
    }
}