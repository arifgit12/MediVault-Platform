package com.app.medivault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "prescriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = "upload_id")
)
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Used for idempotency (duplicate prevention)
    @Column(name = "upload_id", nullable = false, unique = true)
    private String uploadId;

    // Stored file path or cloud URL
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // Patient who owns this prescription
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // OCR / analysis lifecycle
    @Enumerated(EnumType.STRING)
    private AnalysisStatus analysisStatus;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
