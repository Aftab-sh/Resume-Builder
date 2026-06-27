package com.AftabShaikh.resumeBuilderapi.Controller;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Service.UserTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Slf4j
public class TemplatesController {

    private final UserTemplateService userTemplateService;

    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication) {
        // ✅ Filter "User" object set karta hai principal me, "UserPrincipal" nahi
        User user = (User) authentication.getPrincipal();

        List<Map<String, Object>> templates = userTemplateService.getTemplatesForUser(user);

        return ResponseEntity.ok(templates);
    }
}