package com.AftabShaikh.resumeBuilderapi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 This entity stores resume data, including structured sections like skills, education, and projects using JSON fields.”
 */
@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Resume
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    private String thumbnailLink;
    private boolean paid = false;  // default free


    // ================= JSON FIELDS =================

 // getter and setter
    
    public boolean isPaid()
    { 
    	return paid; 
    }
    public void setPaid(boolean paid) { this.paid = paid; 
    }
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Template template;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private ProfileInfo profileInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private ContactInfo contactInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<WorkExperience> workExperience = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<Education> education = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<Skill> skills = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<Project> projects = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<Certification> certification = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<Language> languages = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> interests = new ArrayList<>();

    // ================= TIMESTAMP =================

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==========================================================
    // INNER STATIC CLASSES (Serializable for safety)
    // ==========================================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Template implements Serializable {
        private String theme;
        private List<String> colorPolette;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileInfo implements Serializable {
        private String profilePreviewUrl;
        private String fullName;
        private String designation;
        private String summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactInfo implements Serializable {
        private String email;
        private String phone;
        private String location;
        private String linkedIn;
        private String github;
        private String website;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkExperience implements Serializable {
        private String company;
        private String role;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Education implements Serializable {
        private String degree;
        private String institution;
        private String startDate;
        private String endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Skill implements Serializable {
        private String name;
        private Integer progress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Project implements Serializable {
        private String name;
        private String description;
        private String github;
        private String liveDemo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Certification implements Serializable {
        private String title;
        private String issuer;
        private String year;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Language implements Serializable {
        private String name;
        private Integer progress;
    }
}

/*
 * Ye class kyu use ki gayi hai?

Resume ka complete data store karna
Flexible structure (JSON use karke)
User ke multiple resumes manage karna
 */
/*
 *1. JSON Fields 
“We used JSON columns to store complex and dynamic data like skills, projects, and education.”

 2. @JdbcTypeCode(SqlTypes.JSON)
“Used to map Java objects to JSON columns in the database.”

 3. @CreationTimestamp / @UpdateTimestamp
“Automatically manages created and updated time.”

4. Inner Static Classes
“Used to structure resume sections like Profile, Skills, Education, etc.” 
 
*/

/*
 “I chose JSON for flexibility and faster development, as resume structure can vary between users. It reduces joins and simplifies data handling.”
 */

/*
“This entity is used to store resume data in the database.
It includes basic fields and multiple JSON fields to store structured data like education, skills, and projects.
Using JSON allows flexibility and reduces the need for multiple relational tables.”
*/