package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String full_name;
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleUser roleUser;

    // Гетери і сетери
    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoleUser getRoleUser() { return roleUser; }
    public void setRoleUser(RoleUser roleUser) { this.roleUser = roleUser; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}