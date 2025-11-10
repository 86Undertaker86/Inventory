package com.CourseWork.Inventory.Model;

public class LoginRequest {
    private String username;
    private String password;
    private RoleUser roleUser;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoleUser getRoleUser() { return roleUser; }
    public void setRoleUser(RoleUser roleUser) { this.roleUser = roleUser; }
}