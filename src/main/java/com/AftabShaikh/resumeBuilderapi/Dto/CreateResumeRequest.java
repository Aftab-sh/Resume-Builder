package com.AftabShaikh.resumeBuilderapi.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import com.AftabShaikh.resumeBuilderapi.Entity.Resume;

import java.util.List;

@Data
public class CreateResumeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private Resume.Template template;
    private Resume.ProfileInfo profileInfo;
    private Resume.ContactInfo contactInfo;

    private List<Resume.WorkExperience> workExperience;
    private List<Resume.Education> education;
    private List<Resume.Skill> skills;
    private List<Resume.Project> projects;
    private List<Resume.Certification> certification;
    private List<Resume.Language> languages;
    private List<String> interests;
}