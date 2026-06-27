package com.AftabShaikh.resumeBuilderapi.Service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Dto.CreateResumeRequest;
import com.AftabShaikh.resumeBuilderapi.Entity.Resume;
import com.AftabShaikh.resumeBuilderapi.Repository.ResumeRepository;

import java.util.ArrayList;
import java.util.List;

//“This service manages resume CRUD operations with user-based authorization.”
@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    // ================= CREATE =================

    public Resume createResume(CreateResumeRequest request, Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        Resume newResume = new Resume();
        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());

        setDefaultResumeData(newResume);

        return resumeRepository.save(newResume);
    }

    // ================= DEFAULT DATA =================

    private void setDefaultResumeData(Resume resume) {

        resume.setTemplate(new Resume.Template());
        resume.setProfileInfo(new Resume.ProfileInfo());
        resume.setContactInfo(new Resume.ContactInfo());
        resume.setWorkExperience(new ArrayList<>());
        resume.setEducation(new ArrayList<>());
        resume.setSkills(new ArrayList<>());
        resume.setProjects(new ArrayList<>());
        resume.setCertification(new ArrayList<>());
        resume.setLanguages(new ArrayList<>());
        resume.setInterests(new ArrayList<>());
    }

    // ================= GET ALL =================

    public List<Resume> getUserResumes(Object principal) {

        AuthResponse response = authService.getProfile(principal);

        return resumeRepository
                .findByUserIdOrderByUpdatedAtDesc(response.getId());
    }

    // ================= GET ONE =================

    public Resume getResumeById(Long resumeId, Object principal) {

        AuthResponse response = authService.getProfile(principal);

        return resumeRepository
                .findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }

    // ================= UPDATE =================

    public Resume updateResume(Long id,
                               CreateResumeRequest request,
                               Object principal) {

        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepository
                .findByUserIdAndId(response.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // Safe update
        existingResume.setTitle(request.getTitle());
        existingResume.setTemplate(request.getTemplate());
        existingResume.setProfileInfo(request.getProfileInfo());
        existingResume.setContactInfo(request.getContactInfo());
        existingResume.setWorkExperience(request.getWorkExperience());
        existingResume.setEducation(request.getEducation());
        existingResume.setSkills(request.getSkills());
        existingResume.setProjects(request.getProjects());
        existingResume.setCertification(request.getCertification());
        existingResume.setLanguages(request.getLanguages());
        existingResume.setInterests(request.getInterests());

        return resumeRepository.save(existingResume);
    }

    // ================= DELETE =================

    public void deleteResume(Long resumeId, Object principal) {

        AuthResponse response = authService.getProfile(principal);

        Resume existingResume = resumeRepository
                .findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resumeRepository.delete(existingResume);
    }
}

/*
 * Ye class kyu use ki gayi hai?

Business logic handle karna
User-specific data control karna
Security enforce karna (user apna hi data dekhe)
 */
/*
 * Flow 
 * 

 Create
User info nikala (authService)
Resume object banaya
Default data set kiya
DB me save kiya

 Get All
User ID nikala
DB se us user ke resumes fetch kiye

 Get One
User ID + Resume ID check
Agar match → return
Nahi → error

Update
Existing resume nikala
Fields update ki
Save kiya

 Delete
Resume verify kiya
Delete kar diya
 */

/*
 *  Basic
Q1: @Service kyu use kiya?
“To define business logic layer managed by Spring.”

Q2: RequiredArgsConstructor kya karta hai?
“It automatically creates constructor for final fields.”

Important ( Must)
Q3: authService kyu use kiya?
“To get current logged-in user details.”

Q4: findByUserIdAndId kyu use kiya?
“To ensure user can only access their own data.”

Q5: Default data kyu set kiya?
“To avoid null values and ensure consistent structure.”

 Advanced
Q6: Agar user dusre ka resume access kare to?
“It will throw exception because of userId check.”

Q7: Transaction management ka kya?
“Spring handles it automatically, or we can use @Transactional.”
 */

/*
 Security kaise ensure ki?
“I always fetch data using userId along with resumeId to ensure users can only access their own data.”
 */


/*
 * “This service handles all resume-related operations such as create, read, update, and delete.
It ensures that only the authenticated user can access their data by validating user ID before performing any operation.”
 */
