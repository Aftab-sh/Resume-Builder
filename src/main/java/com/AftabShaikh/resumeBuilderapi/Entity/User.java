package com.AftabShaikh.resumeBuilderapi.Entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class User 
{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
   
    private String profileImageUrl;
    private String subscriptionPlan = "basic";
    private boolean emailVerified = false;
    private String verificationToken;
    private LocalDateTime verificationExpires;
	@CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
	@UpdateTimestamp
    private LocalDateTime updatedAt;
	
	
	@Enumerated(EnumType.STRING)
    private Roles role;


   
}
