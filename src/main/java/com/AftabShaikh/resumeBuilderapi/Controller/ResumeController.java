package com.AftabShaikh.resumeBuilderapi.Controller;

import com.AftabShaikh.resumeBuilderapi.Dto.CreateResumeRequest;
import com.AftabShaikh.resumeBuilderapi.Entity.Resume;
import com.AftabShaikh.resumeBuilderapi.Service.FileUploadService;
import com.AftabShaikh.resumeBuilderapi.Service.ResumeService;
import com.AftabShaikh.resumeBuilderapi.Util.AppConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//“This controller provides REST APIs for resume management with authentication.”
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/resumes")
public class ResumeController
{

    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<Resume> createResume(
            @Valid @RequestBody CreateResumeRequest request,
            Authentication authentication) {

        Resume newResume = resumeService.createResume(
                request,
                authentication.getPrincipal()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    // ================= GET ALL =================

    @GetMapping
    public ResponseEntity<List<Resume>> getUserResumes(
            Authentication authentication) {

        List<Resume> resumes =
                resumeService.getUserResumes(authentication.getPrincipal());

        return ResponseEntity.ok(resumes);
    }

    // ================= GET ONE =================

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResumeById(
            @PathVariable Long id,
            Authentication authentication) {

        Resume resume =
                resumeService.getResumeById(id, authentication.getPrincipal());

        return ResponseEntity.ok(resume);
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(
            @PathVariable Long id,
            @Valid @RequestBody CreateResumeRequest updateRequest,
            Authentication authentication) {

        Resume updatedResume = resumeService.updateResume(
                id,
                updateRequest,
                authentication.getPrincipal()
        );

        return ResponseEntity.ok(updatedResume);
    }

    // ================= UPLOAD IMAGES =================

    @PutMapping("/{id}/upload-images")
    public ResponseEntity<Map<String, String>> uploadResumeImages(
            @PathVariable Long id,
            @RequestPart(value = "thumbnail", required = true) MultipartFile thumbnail,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Authentication authentication) throws IOException {

        Map<String, String> response =
                fileUploadService.uploadResumeImages(
                        id,
                        authentication.getPrincipal(),
                        thumbnail,
                        profileImage
                );

        return ResponseEntity.ok(response);
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteResume(
            @PathVariable Long id,
            Authentication authentication)
    {

        resumeService.deleteResume(id, authentication.getPrincipal());

        return ResponseEntity.ok(
                Map.of("message", "Resume deleted successfully")
        );
    }
}

/*Purpose:

Client (React/Postman) se request lena
Service layer ko call karna
Response return karna

Ye API entry point hai
 */

/*
 * Flow samjho (Very Important )

 Interview me ye bolna:

Client request bhejta hai
Controller receive karta hai
Authentication se user nikalta hai
Service ko call karta hai
Service DB se data handle karti hai
Controller response return karta hai
 */

/*
 * 1. @RestController
“Used to create REST APIs that return JSON responses.”

2. @RequestMapping
“Defines base URL for all APIs.”

 3. @PostMapping / @GetMapping / @PutMapping / @DeleteMapping
“Used for CRUD operations.”

 4. Authentication
“Used to get current logged-in user.”

5. @RequestBody
“Used to receive JSON data from client.”

 6. @RequestPart ( Important)
“Used to handle file upload (multipart data).”

 7. ResponseEntity
“Used to customize HTTP response (status + body).”
*/

/*
 * 1: Controller ka role kya hai?
“It handles HTTP requests and returns responses.”

Q2: @RestController vs @Controller?
“@RestController returns JSON, @Controller returns views.”

 Important ( Must)
Q3: Authentication object kya deta hai?
“It provides details of the currently logged-in user.”

Q4: @Valid kyu use kiya?
“To validate request data.”

Q5: ResponseEntity kyu use kiya?
“To control HTTP status and response body.”

 Advanced
Q6: @RequestPart vs @RequestBody?
“@RequestBody for JSON, @RequestPart for file uploads.”

Q7: Agar authentication null ho to?
“User is not logged in, request will fail.”

Q8: HTTP status codes kyu use kiye?
“To follow REST standards.”
 */

/*
This controller handles all resume-related APIs.
It receives requests from the client, extracts the authenticated user, and delegates operations to the service layer.
It returns appropriate responses with HTTP status codes.”
*/