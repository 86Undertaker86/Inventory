package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.User;
import com.CourseWork.Inventory.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRoleUser(updatedUser.getRoleUser());
            existingUser.setFull_name(updatedUser.getFull_name());
            existingUser.setEmail(updatedUser.getEmail());

            // ⚙️ Якщо поле пароля не порожнє — оновлюємо
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            userRepository.save(existingUser);
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}