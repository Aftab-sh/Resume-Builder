package com.AftabShaikh.resumeBuilderapi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
   User  findByEmail(String email);
    Boolean existsByEmail(String email);
    
   Optional<User>findByVerificationToken(String verificationToken);
}
