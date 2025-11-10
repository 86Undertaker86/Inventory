package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new user.
     * Password is hashed using BCrypt before saving.
     */
    public void createUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    /**
     * Updates an existing user's information.
     * - Updates username, role, full name, email
     * - Hashes new password if provided
     */
    public void updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRoleUser(updatedUser.getRoleUser());
            existingUser.setFull_name(updatedUser.getFull_name());
            existingUser.setEmail(updatedUser.getEmail());

            // Hash the password only if a new one is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(existingUser);
        }
    }

    /**
     * Deletes a user by their ID.
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}