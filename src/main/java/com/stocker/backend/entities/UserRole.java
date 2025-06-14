package com.stocker.backend.entities;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    MOD("mod"),
    GUEST("guest");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
