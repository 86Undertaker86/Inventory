package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Головна сторінка адміністратора
    @GetMapping
    public String userPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "UserPage";
    }

    // Додавання користувача
    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {
        userService.createUser(user);
        return "redirect:/admin/user?success";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    // Відкриття форми редагування
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("editUser", userService.getUserById(id));
        return "UserPage";
    }

    // Оновлення користувача
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("editUser") User updatedUser) {
        userService.updateUser(id, updatedUser);
        return "redirect:/admin/user?updated";
    }

    // Видалення користувача
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/user?deleted";
    }
}