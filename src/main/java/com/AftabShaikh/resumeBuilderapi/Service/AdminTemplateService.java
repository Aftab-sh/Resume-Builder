package com.AftabShaikh.resumeBuilderapi.Service;

import com.AftabShaikh.resumeBuilderapi.Entity.Template;
import com.AftabShaikh.resumeBuilderapi.Repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTemplateService {

    private final TemplateRepository templateRepository;

    public Template createTemplate(Template template) {
        // ✅ templateId backend khud generate karega, frontend se nahi aana chahiye
        template.setTemplateId(UUID.randomUUID().toString().substring(0, 8));
        template.setActive(true);
        return templateRepository.save(template);
    }

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template updateTemplate(Long id, Template updated) {
        Template existing = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        existing.setName(updated.getName());
        existing.setPrimaryColor(updated.getPrimaryColor());
        existing.setFontFamily(updated.getFontFamily());
        existing.setLayoutType(updated.getLayoutType());
        existing.setPremium(updated.isPremium());
        existing.setActive(updated.isActive());
        existing.setThumbnailUrl(updated.getThumbnailUrl());

        return templateRepository.save(existing);
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
}