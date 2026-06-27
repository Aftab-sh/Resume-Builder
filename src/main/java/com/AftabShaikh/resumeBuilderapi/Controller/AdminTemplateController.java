package com.AftabShaikh.resumeBuilderapi.Controller;

import com.AftabShaikh.resumeBuilderapi.Entity.Template;
import com.AftabShaikh.resumeBuilderapi.Service.AdminTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/templates")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminTemplateController {

    private final AdminTemplateService adminTemplateService;

    @PostMapping("/create")
    public ResponseEntity<?> createTemplate(@RequestBody Template template) {
        Template saved = adminTemplateService.createTemplate(template);
        return ResponseEntity.ok(Map.of("message", "Template created!", "template", saved));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Template>> getAllTemplates() {
        return ResponseEntity.ok(adminTemplateService.getAllTemplates());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable Long id, @RequestBody Template template) {
        Template updated = adminTemplateService.updateTemplate(id, template);
        return ResponseEntity.ok(Map.of("message", "Template updated!", "template", updated));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long id) {
        adminTemplateService.deleteTemplate(id);
        return ResponseEntity.ok(Map.of("message", "Template deleted!"));
    }
}