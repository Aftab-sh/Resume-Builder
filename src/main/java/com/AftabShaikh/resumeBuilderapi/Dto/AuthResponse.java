package com.AftabShaikh.resumeBuilderapi.Dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class AuthResponse {
    private Long id; 
    private String name;
    private String email;
    private String profileImageUrl;
    private String subscriptionPlan;
    private Boolean emailVerified;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;  // ✅ Add karo


 
}
