package com.AftabShaikh.resumeBuilderapi.Entity;

public enum Permission {
    // === USER PERMISSIONS ===
    USER_READ,
    USER_WRITE,
    USER_DELETE,
    USER_UPDATE,
    USER_PAY_PAYMENT,
    
    // ✅ ADD THESE - Resume permissions
    USER_RESUME_READ,
    USER_RESUME_WRITE,
    USER_RESUME_UPDATE,
    USER_RESUME_DELETE,
    
    // === ADMIN PERMISSIONS ===
    ADMIN_TEMPLATE_CREATE,
    ADMIN_TEMPLATE_READ,  
    ADMIN_TEMPLATE_UPDATE, 
    ADMIN_TEMPLATE_DELETE, 
    ADMIN_VIEW_USERS
}