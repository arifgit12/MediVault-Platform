package com.app.medivault.service;

import com.app.medivault.dtos.MedicineDto;
import com.app.medivault.dtos.PrescriptionRequest;
import com.app.medivault.dtos.PrescriptionResponse;
import com.app.medivault.entity.*;
import com.app.medivault.repository.MedicineRepository;
import com.app.medivault.repository.PatientRepository;
import com.app.medivault.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final GoogleVisionOcrService ocrService;
    private final PrescriptionTextParser parser;
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;

    private static final String UPLOAD_DIR = "uploads/prescriptions/";

    @Autowired
    public PrescriptionService(GoogleVisionOcrService ocrService,
                               PrescriptionTextParser parser,
                               PrescriptionRepository prescriptionRepository,
                               PatientRepository patientRepository,
                               MedicineRepository medicineRepository) {
        this.ocrService = ocrService;
        this.parser = parser;
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
        
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            // Directory creation failed
        }
    }

    @Transactional
    public PrescriptionResponse uploadPrescription(String uploadId, Long patientId, MultipartFile file, Long userId) throws IOException {
        // Check for duplicate upload using uploadId (idempotency)
        Optional<Prescription> existingPrescription = prescriptionRepository.findByUploadId(uploadId);
        if (existingPrescription.isPresent()) {
            return mapToResponse(existingPrescription.get());
        }

        // Verify patient belongs to user
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to patient");
        }

        // Save file
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create prescription
        Prescription prescription = new Prescription();
        prescription.setUploadId(uploadId);
        prescription.setImageUrl(filePath.toString());
        prescription.setPatient(patient);
        prescription.setAnalysisStatus(AnalysisStatus.PENDING);

        prescription = prescriptionRepository.save(prescription);

        // Start OCR processing asynchronously (in real app, use @Async or message queue)
        try {
            processOcr(prescription.getId(), file.getBytes());
        } catch (Exception e) {
            // OCR failed, but prescription is saved
            prescription.setAnalysisStatus(AnalysisStatus.FAILED);
            prescriptionRepository.save(prescription);
        }

        return mapToResponse(prescription);
    }

    @Transactional
    public void processOcr(Long prescriptionId, byte[] imageBytes) throws IOException {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescription.setAnalysisStatus(AnalysisStatus.PROCESSING);
        prescriptionRepository.save(prescription);

        try {
            // Extract text using OCR
            String rawText = ocrService.extractText(imageBytes);
            prescription.setRawOcrText(rawText);

            // Parse medicines from text
            List<Medicine> medicines = parser.parseMedicines(rawText);
            
            // Associate medicines with prescription
            for (Medicine medicine : medicines) {
                medicine.setPrescription(prescription);
            }
            
            prescription.setMedicines(medicines);
            prescription.setAnalysisStatus(AnalysisStatus.COMPLETED);

            prescriptionRepository.save(prescription);
        } catch (Exception e) {
            prescription.setAnalysisStatus(AnalysisStatus.FAILED);
            prescriptionRepository.save(prescription);
            throw e;
        }
    }

    @Transactional
    public PrescriptionResponse updatePrescription(Long prescriptionId, PrescriptionRequest request, Long userId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Verify ownership
        if (!prescription.getPatient().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        prescription.setDoctorName(request.getDoctorName());
        prescription.setHospitalName(request.getHospitalName());
        prescription.setPrescriptionDate(request.getPrescriptionDate());
        prescription.setDiagnosis(request.getDiagnosis());

        // Update medicines
        if (request.getMedicines() != null) {
            // Remove old medicines
            prescription.getMedicines().clear();

            // Add new medicines
            for (MedicineDto dto : request.getMedicines()) {
                Medicine medicine = new Medicine();
                medicine.setName(dto.getName());
                medicine.setDosage(dto.getDosage());
                medicine.setFrequency(dto.getFrequency());
                medicine.setDuration(dto.getDuration());
                medicine.setPrescription(prescription);
                prescription.getMedicines().add(medicine);
            }
        }

        prescription = prescriptionRepository.save(prescription);

        return mapToResponse(prescription);
    }

    public List<PrescriptionResponse> getPrescriptionsByPatient(Long patientId, Long userId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Verify ownership
        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return prescriptions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PrescriptionResponse getPrescriptionById(Long prescriptionId, Long userId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Verify ownership
        if (!prescription.getPatient().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(prescription);
    }

    @Transactional
    public void deletePrescription(Long prescriptionId, Long userId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Verify ownership
        if (!prescription.getPatient().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Delete file
        try {
            Files.deleteIfExists(Paths.get(prescription.getImageUrl()));
        } catch (IOException e) {
            // File deletion failed
        }

        prescriptionRepository.delete(prescription);
    }

    private PrescriptionResponse mapToResponse(Prescription prescription) {
        PrescriptionResponse response = new PrescriptionResponse();
        response.setId(prescription.getId());
        response.setUploadId(prescription.getUploadId());
        response.setImageUrl(prescription.getImageUrl());
        response.setPatientId(prescription.getPatient().getId());
        response.setPatientName(prescription.getPatient().getName());
        response.setDoctorName(prescription.getDoctorName());
        response.setHospitalName(prescription.getHospitalName());
        response.setPrescriptionDate(prescription.getPrescriptionDate());
        response.setDiagnosis(prescription.getDiagnosis());
        
        List<MedicineDto> medicineDtos = prescription.getMedicines().stream()
                .map(m -> new MedicineDto(m.getId(), m.getName(), m.getDosage(), m.getFrequency(), m.getDuration()))
                .collect(Collectors.toList());
        response.setMedicines(medicineDtos);
        
        response.setAnalysisStatus(prescription.getAnalysisStatus() != null ? prescription.getAnalysisStatus().name() : null);
        response.setRiskLevel(prescription.getRiskLevel() != null ? prescription.getRiskLevel().name() : null);
        response.setAlerts(prescription.getAlerts());
        response.setCreatedAt(prescription.getCreatedAt());
        response.setUpdatedAt(prescription.getUpdatedAt());
        
        return response;
    }
}


