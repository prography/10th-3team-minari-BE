package com.prography.minari.user.enums;

public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
