package com.AftabShaikh.resumeBuilderapi.Controller;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Service.UserTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class UserTemplateController {

    private final UserTemplateService userTemplateService;

    @GetMapping("/list")
    public ResponseEntity<?> getUserDashboardTemplates(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userTemplateService.getTemplatesForUser(user));
    }
}