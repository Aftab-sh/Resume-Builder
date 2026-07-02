package com.AftabShaikh.resumeBuilderapi.Controller;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Dto.LoginRequest;
import com.AftabShaikh.resumeBuilderapi.Dto.RegisterRequest;
import com.AftabShaikh.resumeBuilderapi.Service.AuthService;
import com.AftabShaikh.resumeBuilderapi.Service.FileUploadService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.AftabShaikh.resumeBuilderapi.Util.AppConstants;
import com.AftabShaikh.resumeBuilderapi.Util.JwtUtil;

@CrossOrigin(origins = {"https://resume-builder-frontent-31bbar63g-aftab-projects1.vercel.app","http://localhost:5173", "http://localhost:5174"}) // React Ports configured
@RestController 
@RequestMapping("/api/auth") 
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final FileUploadService fileUploadService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, FileUploadService fileUploadService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.fileUploadService = fileUploadService;
        this.jwtUtil = jwtUtil;  
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Inside AuthController - register(): {}", request);

        try {
            AuthResponse response = authService.register(request);
            log.info("Response from service: {}", response);

            // Overloaded system supporting initial string registry token signature
            String token = jwtUtil.generateToken(response.getId().toString()); 
            response.setToken(token);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        log.info("Inside AuthController - verifyEmail: {}", token);
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Email verified successfully"));
    }
    
    @PostMapping("/upload-image")
    public ResponseEntity<?> upladImage(@RequestPart("image") MultipartFile file) throws IOException {
        log.info("Inside AuthController - uploadImage()");
        Map<String, String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }
        authService.resendVerification(email);
        return ResponseEntity.ok(Map.of("success", true, "message", "Verification email sent"));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        Object principalObject = authentication.getPrincipal();
        AuthResponse currentProfile = authService.getProfile(principalObject);
        return ResponseEntity.ok(currentProfile);
    }
}