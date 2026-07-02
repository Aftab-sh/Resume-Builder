package com.AftabShaikh.resumeBuilderapi.Service;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.Roles; // 🚀 Roles Import kiya
import com.AftabShaikh.resumeBuilderapi.Entity.UserPrincipal; // 🚀 UserPrincipal Import kiya
import com.AftabShaikh.resumeBuilderapi.Exception.ResourceExistsException;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Dto.LoginRequest;
import com.AftabShaikh.resumeBuilderapi.Dto.RegisterRequest;
import com.AftabShaikh.resumeBuilderapi.Repository.UserRepository;
import com.AftabShaikh.resumeBuilderapi.Util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    private final EmailService emailService;

    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    
    
    public AuthResponse register(RegisterRequest request) {
        log.info("Inside AuthService register() for email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("User already exists with this email");
        }

        User newUser = toEntity(request);
        User savedUser = userRepository.save(newUser);

        // Email async send karo — fail hone pe registration block nahi hogi
        try {
            sendVerificationEmail(savedUser);
            log.info("Verification email sent successfully");
        } catch (Exception e) {
            log.warn("Email send failed (non-blocking): {}", e.getMessage());
        }

        return toResponse(savedUser);
    }
    
    

//    public AuthResponse register(RegisterRequest request) 
//    {
//        log.info("Inside AuthService register() for email: {}", request.getEmail());
//        
//        if (userRepository.existsByEmail(request.getEmail()))
//        {
//            throw new ResourceExistsException("User already exists with this email");
//        }
//        
//        User newUser = toEntity(request);
//        User savedUser = userRepository.save(newUser);
//        
//        log.info("send email in registation ");
//        sendVerificationEmail(savedUser); 
//        log.info("successfully send email ");
//        
//        return toResponse(savedUser);
//    }
    
    
    
    

    private void sendVerificationEmail(User user) {
        log.info("inside AuthService - sendVerificationEmail(): {}", user); 
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + user.getVerificationToken();
            log.info("Generated verification link: {}", link);

            String html = "<div style='font-family: sans-serif; padding: 20px; border: 1px solid #eee; border-radius: 10px;'>" +
                    "<h2>Verify your email</h2>" +
                    "<p>Hi " + user.getName() + ", please confirm your email to activate your account.</p>" +
                    "<p style='margin: 25px 0;'>" +
                    "<a href=\"" + link + "\" style='background-color: #6366f1; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>" +
                    "Verify Email</a>" +
                    "</p>" +
                    "<p>If the button doesn't work, copy and paste this link in your browser:</p>" +
                    "<p style='color: #6366f1;'>" + link + "</p>" +
                    "<p>This link will expire in 24 hours.</p>" +
                    "</div>";

            emailService.sendHtmlEmail(user.getEmail(), "Verify your email", html);
            log.info("Email successfully sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Exception occured at sendVerificationEmail(): {}", e.getMessage());
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    private AuthResponse toResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setId(user.getId()); 
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setSubscriptionPlan(user.getSubscriptionPlan());
        response.setRole(user.getRole().name()); // ✅ Ye line add karo

        response.setEmailVerified(user.isEmailVerified());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    private User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setSubscriptionPlan("Basic");
//        user.setEmailVerified(false);
//        user.setVerificationToken(UUID.randomUUID().toString());
//        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        
        
        // 🚀 FIXED 1: Registration par by-default USER role assign kiya
        user.setRole(Roles.USER); 
        
        return user; 
    }
    
    public void verifyEmail(String token) {
        log.info("Inside authService: verifyEmail(): {}", token);
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));
     
        if (user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired. Please request a new one.");
        }
        
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    // 🚀 FIXED 2: Dynamic Roles packing for Shared Login Page
    public AuthResponse login(LoginRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser == null) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
        
        if (!existingUser.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }
        
        // 🚀 UserPrincipal banaya database ke live user details aur roles se
        UserPrincipal userPrincipal = new UserPrincipal(existingUser);
        
        // Dynamic Token generated (is token me authority user ki hamesha live packed rahegi)
        String token = jwtUtil.generateToken(userPrincipal);
                
        AuthResponse response = toResponse(existingUser);
        response.setToken(token);
        return response;
    }

    public void resendVerification(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }       
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }
        
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        sendVerificationEmail(user);
    }

//    public AuthResponse getProfile(Object principalObject) {
//        // 🚀 FIXED 3: PrincipalObject hamesha UserPrincipal hoga, isiliye uski entity extract ki
//        if (principalObject instanceof UserPrincipal) {
//            User existingUser = userRepository.findByEmail(((UserPrincipal) principalObject).getUsername());
//            return toResponse(existingUser);
//        }
//        throw new RuntimeException("Unauthorized principal type context");
//    }
    
    public AuthResponse getProfile(Object principalObject) {
        
        // User object directly aata hai JwtFilter se
        if (principalObject instanceof User user) {
            return toResponse(user);
        }
        
        // Fallback: UserPrincipal ke liye bhi handle karo
        if (principalObject instanceof UserPrincipal userPrincipal) {
            User existingUser = userRepository.findByEmail(userPrincipal.getUsername());
            return toResponse(existingUser);
        }
        
        throw new RuntimeException("Unauthorized principal type context");
    }
}