package com.AftabShaikh.resumeBuilderapi.Service;

import com.AftabShaikh.resumeBuilderapi.Entity.Template;
import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTemplateService {

    private final TemplateRepository templateRepository;

    // ✅ Ab seedha "User" leta hai, UserRepository.findByEmail() ki zaroorat nahi
    // kyunki JwtAuthenticationFilter pehle hi live User object SecurityContext me daal chuka hai
    public List<Map<String, Object>> getTemplatesForUser(User user) {
        String userPlan = (user != null && user.getSubscriptionPlan() != null) ? user.getSubscriptionPlan() : "basic";

        List<Template> allTemplates = templateRepository.findByActiveTrue(); // ✅ sirf active templates, findAll() nahi

        return allTemplates.stream().map(template -> {
            boolean isLocked = template.isPremium() && !"premium".equalsIgnoreCase(userPlan);

            Map<String, Object> templateMap = new HashMap<>();
            templateMap.put("id", template.getId());
            templateMap.put("name", template.getName());
            templateMap.put("primaryColor", template.getPrimaryColor());
            templateMap.put("fontFamily", template.getFontFamily());
            templateMap.put("layoutType", template.getLayoutType());
            templateMap.put("isPremium", template.isPremium());
            templateMap.put("isLocked", isLocked);
            return templateMap;
        }).collect(Collectors.toList());
    }
}