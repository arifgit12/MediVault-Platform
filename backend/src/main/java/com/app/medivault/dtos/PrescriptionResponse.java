package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private String uploadId;
    private String imageUrl;
    private Long patientId;
    private String patientName;
    private String doctorName;
    private String hospitalName;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private List<MedicineDto> medicines;
    private String analysisStatus;
    private String riskLevel;
    private String alerts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
