package com.AftabShaikh.resumeBuilderapi.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.AftabShaikh.resumeBuilderapi.Repository.UserRepository;
import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.UserPrincipal;

@Service 
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
 
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;     
    }
 
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        // 🚀 Fixed: Ab constructor bilkul sahi match ho raha hai
        return new UserPrincipal(user);
    }
}