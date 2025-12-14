package com.app.medivault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "prescriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = "upload_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Extracted data
    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "prescription_date")
    private LocalDate prescriptionDate;

    @Column(length = 5000)
    private String diagnosis;

    @Column(name = "raw_ocr_text", length = 5000)
    private String rawOcrText;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicine> medicines = new ArrayList<>();

    // Analysis results
    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Column(length = 2000)
    private String alerts;

    // Audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (analysisStatus == null) {
            analysisStatus = AnalysisStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
