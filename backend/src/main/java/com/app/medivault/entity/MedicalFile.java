package com.app.medivault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upload_id", nullable = false, unique = true)
    private String uploadId;

    // Patient who owns this medical file
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Original file name
    @Column(name = "original_filename")
    private String originalFilename;

    // Original file type (pdf, txt, jpg, png, etc.)
    @Column(name = "original_filetype")
    private String originalFiletype;

    // Final PDF file path (after conversion)
    @Column(name = "pdf_url", nullable = false)
    private String pdfUrl;

    // File category/type
    @Column(name = "file_category")
    private String fileCategory; // prescription, lab_report, medical_record, etc.

    // Description or notes
    @Column(length = 1000)
    private String description;

    // Processing status
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status")
    private FileProcessingStatus processingStatus;

    // Audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (processingStatus == null) {
            processingStatus = FileProcessingStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
