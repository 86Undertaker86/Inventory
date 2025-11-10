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

    /**
     * Returns all available roles for the dropdown on the login page.
     */
    public List<RoleUser> getAllRoles() {
        return Arrays.asList(RoleUser.values());
    }

    /**
     * Validates a user's login request.
     * Checks:
     *  1. If the user exists
     *  2. If the password matches using BCrypt
     *  3. If the selected role matches the user's role
     *
     * Returns a success string if validation passes,
     * or an error message describing the failure.
     */
    public String validateLogin(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return "User not found";
        }

        User user = userOpt.get();

        // Check password using BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return "Invalid password";
        }

        // Check role
        if (user.getRoleUser() != loginRequest.getRoleUser()) {
            return "Incorrect role for this user";
        }

        return "success";
    }
}