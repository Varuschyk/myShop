package com.alexvar.springboot_rest.model;

public enum Permission {
    DEVELOPERS_READ("developer:read"),
    DEVELOPERS_WRITE("developer:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
