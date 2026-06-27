package com.AftabShaikh.resumeBuilderapi.Repository;

import com.AftabShaikh.resumeBuilderapi.Entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByActiveTrue();
    Optional<Template> findByTemplateId(String templateId);
}