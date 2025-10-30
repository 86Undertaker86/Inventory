package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.LoginRequest;
import com.CourseWork.Inventory.Model.RoleUser;
import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Отримати всі ролі (для випадаючого списку на сторінці)
    public List<RoleUser> getAllRoles() {
        return Arrays.asList(RoleUser.values());
    }

    // Перевірка логіну користувача
    public String validateLogin(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return "Користувача не знайдено";
        }

        User user = userOpt.get();

        // ✅ Перевірка пароля через BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return "Невірний пароль";
        }

        // ✅ Перевірка ролі
        if (user.getRoleUser() != loginRequest.getRoleUser()) {
            return "Невірна роль для цього користувача";
        }

        return "success";
    }
}