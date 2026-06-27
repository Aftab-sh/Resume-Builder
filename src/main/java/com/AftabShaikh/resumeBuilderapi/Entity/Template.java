package com.AftabShaikh.resumeBuilderapi.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String templateId;  // "01", "02", "04"

    private String name;        // "Modern", "Classic"

    private String primaryColor;
    private String fontFamily;
    private String layoutType;

    private boolean premium;    // true = sirf premium users

    private boolean active;     // true = visible, false = hidden

    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String htmlContent; // optional
}