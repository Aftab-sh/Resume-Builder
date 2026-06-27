package com.AftabShaikh.resumeBuilderapi.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest 
{
//iss ki ye charo field fill krna mendetory hi he 
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 15, message = "Name must be between 2 and 15 characters")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;

    private String profileImageUrl; // 'p' small kar diya naming convention ke liye

  // with @Enumerated(EnumType.STRING) validation
    // 1. No-Args Constructor
    public RegisterRequest() {
    }

    // 2. All-Args Constructor
    public RegisterRequest(String email, String name, String password, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    // 3. Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // 4. ToString for Debugging
    @Override
    public String toString() {
        return "RegisterRequest [email=" + email + ", name=" + name + ", profileImageUrl=" + profileImageUrl + "]";
    }
}
