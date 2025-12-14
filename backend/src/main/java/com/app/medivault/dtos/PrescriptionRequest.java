package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {
    private String uploadId;
    private Long patientId;
    private String doctorName;
    private String hospitalName;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private List<MedicineDto> medicines;
}
