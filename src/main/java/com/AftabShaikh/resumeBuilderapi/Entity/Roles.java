package com.AftabShaikh.resumeBuilderapi.Entity;

import java.util.Set;

public enum Roles {
    USER(Set.of(
        Permission.USER_READ,
        Permission.USER_WRITE,
        Permission.USER_DELETE,
        Permission.USER_UPDATE,
        Permission.USER_PAY_PAYMENT,
        Permission.USER_RESUME_READ,
        Permission.USER_RESUME_WRITE,
        Permission.USER_RESUME_UPDATE,
        Permission.USER_RESUME_DELETE
    )),  // ✅ USER ke baad comma - next enum constant hai

    ADMIN(Set.of(
        Permission.ADMIN_TEMPLATE_CREATE,
        Permission.ADMIN_TEMPLATE_READ,
        Permission.ADMIN_TEMPLATE_UPDATE,
        Permission.ADMIN_TEMPLATE_DELETE,
        Permission.ADMIN_VIEW_USERS,
        Permission.USER_RESUME_READ,
        Permission.USER_RESUME_WRITE,
        Permission.USER_RESUME_UPDATE,
        Permission.USER_RESUME_DELETE
    )); // ✅ ADMIN ke baad semicolon - last enum constant hai

    private final Set<Permission> permission;

    Roles(Set<Permission> permission) {
        this.permission = permission;
    }

    public Set<Permission> getPermission() {
        return permission;
    }
}