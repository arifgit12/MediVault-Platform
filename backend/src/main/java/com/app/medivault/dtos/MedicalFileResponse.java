package com.app.medivault.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFileResponse {
    private Long id;
    private String uploadId;
    private Long patientId;
    private String patientName;
    private String originalFilename;
    private String originalFiletype;
    private String pdfUrl;
    private String fileCategory;
    private String description;
    private String processingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
