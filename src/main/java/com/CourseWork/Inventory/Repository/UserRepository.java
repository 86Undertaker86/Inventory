package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     * Returns an Optional containing the User if found.
     * Useful for authentication and login operations.
     */
    Optional<User> findByUsername(String username);
}