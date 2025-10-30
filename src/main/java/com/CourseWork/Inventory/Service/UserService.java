package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ✅ додано

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

    // ✅ Додавання нового користувача з хешуванням паролю
    public void createUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    // ✅ Оновлення користувача з хешуванням паролю при зміні
    public void updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRoleUser(updatedUser.getRoleUser());
            existingUser.setFull_name(updatedUser.getFull_name());
            existingUser.setEmail(updatedUser.getEmail());

            // Якщо пароль не порожній — хешуємо новий
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(existingUser);
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}