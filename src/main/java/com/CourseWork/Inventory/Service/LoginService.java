package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.LoginRequest;
import com.CourseWork.Inventory.Model.RoleUser;
import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<RoleUser> getAllRoles() {
        return Arrays.asList(RoleUser.values());
    }

    public String validateLogin(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return "Користувача не знайдено";
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return "Невірний пароль";
        }

        if (user.getRoleUser() != loginRequest.getRoleUser()) {
            return "Невірна роль для цього користувача";
        }

        return "success";
    }
}