package com.AftabShaikh.resumeBuilderapi.Entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    
    private final User user;

    // 🚀 Sahi Constructor: Sirf user pass karo, role entity se khud nikal aayega
    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        
        Roles userRole = user.getRole(); // User entity se roles nikala
        
        if (userRole != null) {
            // 1. Role add kiya (e.g., ROLE_USER)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
            
            // 2. Saari permissions ko convert karke add kiya
            if (userRole.getPermission() != null) {
                Set<SimpleGrantedAuthority> permissionAuthorities = userRole.getPermission().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .collect(Collectors.toSet());
                authorities.addAll(permissionAuthorities);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Login email se ho raha hai
    }
}