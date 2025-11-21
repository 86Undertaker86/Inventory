package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.LoginRequest;
import com.CourseWork.Inventory.Service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Displays the login page and initializes an empty LoginRequest object.
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "LoginPage";
    }

    @GetMapping("/logout")
    public String BackToLoginPage() {
        return "redirect:/login";
    }

    /**
     * Processes login form submission.
     * Validates credentials and redirects users to their respective dashboards
     * based on their role (ADMIN, MANAGER, or LOADER).
     * If validation fails, returns to the login page with an error message.
     */
    @PostMapping("/login")
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
