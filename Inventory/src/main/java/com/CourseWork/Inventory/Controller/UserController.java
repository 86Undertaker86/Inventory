package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the main page for managing users.
     * Loads all existing users and provides an empty form for adding a new one.
     */
    @GetMapping
    public String userPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "UserPage";
    }

    /**
     * Handles adding a new user to the system.
     * Redirects back to the user page with a success message.
     */
    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {
        userService.createUser(user);
        return "redirect:/admin/users?success";
    }

    /**
     * Returns user data by ID in JSON format.
     * Used for dynamic data fetching via AJAX.
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    /**
     * Opens the edit form for a specific user.
     * Populates the form with existing user data.
     */
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("editUser", userService.getUserById(id));
        return "UserPage";
    }

    /**
     * Updates an existing user's information.
     * Redirects to the main user page with an update confirmation.
     */
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("editUser") User updatedUser) {
        userService.updateUser(id, updatedUser);
        return "redirect:/admin/users?updated";
    }

    /**
     * Deletes a user by ID.
     * Redirects to the user page with a deletion confirmation.
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?deleted";
    }
}