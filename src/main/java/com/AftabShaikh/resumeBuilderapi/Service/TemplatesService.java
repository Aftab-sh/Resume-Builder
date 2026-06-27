package com.AftabShaikh.resumeBuilderapi.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Entity.Template;
import com.AftabShaikh.resumeBuilderapi.Repository.TemplateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {

    private final AuthService authService;
    private final TemplateRepository templateRepository;

    public Map<String, Object> getTemplates(Object principal) {
        AuthResponse authResponse = authService.getProfile(principal);
        boolean isPremium = "premium".equalsIgnoreCase(authResponse.getSubscriptionPlan());

        // ✅ Database se sirf active templates
        List<Template> allTemplates = templateRepository.findByActiveTrue();

        // ✅ Filter: premium templates sirf premium users ko
        List<String> availableTemplateIds = allTemplates.stream()
                .filter(t -> !t.isPremium() || isPremium)
                .map(Template::getTemplateId)
                .collect(Collectors.toList());

        Map<String, Object> restrictions = new HashMap<>();
        restrictions.put("availableTemplates", availableTemplateIds);
        restrictions.put("allTemplates", allTemplates.stream().map(Template::getTemplateId).collect(Collectors.toList()));
        restrictions.put("subscriptionPlan", authResponse.getSubscriptionPlan());
        restrictions.put("isPremium", isPremium);

        return restrictions;
    }
}