package com.app.medivault.service;

import com.app.medivault.dtos.MedicalFileResponse;
import com.app.medivault.entity.*;
import com.app.medivault.repository.MedicalFileRepository;
import com.app.medivault.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalFileService {

    private final MedicalFileRepository medicalFileRepository;
    private final PatientRepository patientRepository;
    private final PdfConversionService pdfConversionService;

    private static final String UPLOAD_DIR = "uploads/medical-files/";

    @Autowired
    public MedicalFileService(MedicalFileRepository medicalFileRepository,
                              PatientRepository patientRepository,
                              PdfConversionService pdfConversionService) {
        this.medicalFileRepository = medicalFileRepository;
        this.patientRepository = patientRepository;
        this.pdfConversionService = pdfConversionService;
        
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            // Directory creation failed
        }
    }

    @Transactional
    public MedicalFileResponse uploadMedicalFile(String uploadId, Long patientId, MultipartFile file, 
                                                  String category, String description, Long userId) throws IOException {
        // Check for duplicate upload using uploadId (idempotency)
        Optional<MedicalFile> existingFile = medicalFileRepository.findByUploadId(uploadId);
        if (existingFile.isPresent()) {
            return mapToResponse(existingFile.get());
        }

        // Verify patient belongs to user
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to patient");
        }

        String originalFilename = file.getOriginalFilename();
        String originalFiletype = getFileExtension(originalFilename);

        // Create medical file entity
        MedicalFile medicalFile = new MedicalFile();
        medicalFile.setUploadId(uploadId);
        medicalFile.setPatient(patient);
        medicalFile.setOriginalFilename(originalFilename);
        medicalFile.setOriginalFiletype(originalFiletype);
        medicalFile.setFileCategory(category);
        medicalFile.setDescription(description);
        medicalFile.setProcessingStatus(FileProcessingStatus.PROCESSING);

        medicalFile = medicalFileRepository.save(medicalFile);

        // Process file based on type
        try {
            byte[] pdfBytes = processFile(file);
            
            // Save PDF
            String pdfFileName = UUID.randomUUID().toString() + ".pdf";
            Path pdfPath = Paths.get(UPLOAD_DIR + pdfFileName);
            Files.write(pdfPath, pdfBytes);
            
            medicalFile.setPdfUrl(pdfPath.toString());
            medicalFile.setProcessingStatus(FileProcessingStatus.COMPLETED);
        } catch (Exception e) {
            medicalFile.setProcessingStatus(FileProcessingStatus.FAILED);
            medicalFileRepository.save(medicalFile);
            throw new RuntimeException("File processing failed: " + e.getMessage());
        }

        medicalFile = medicalFileRepository.save(medicalFile);
        return mapToResponse(medicalFile);
    }

    @Transactional
    public MedicalFileResponse uploadMultipleMedicalFiles(String uploadId, Long patientId, 
                                                          List<MultipartFile> files, String category, 
                                                          String description, Long userId) throws IOException {
        // Check for duplicate upload
        Optional<MedicalFile> existingFile = medicalFileRepository.findByUploadId(uploadId);
        if (existingFile.isPresent()) {
            return mapToResponse(existingFile.get());
        }

        // Verify patient belongs to user
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to patient");
        }

        // Create medical file entity
        MedicalFile medicalFile = new MedicalFile();
        medicalFile.setUploadId(uploadId);
        medicalFile.setPatient(patient);
        medicalFile.setOriginalFilename("Multiple files (" + files.size() + " files)");
        medicalFile.setOriginalFiletype("merged");
        medicalFile.setFileCategory(category);
        medicalFile.setDescription(description);
        medicalFile.setProcessingStatus(FileProcessingStatus.PROCESSING);

        medicalFile = medicalFileRepository.save(medicalFile);

        // Process and merge files
        try {
            List<byte[]> imageBytesList = new ArrayList<>();
            List<byte[]> pdfBytesList = new ArrayList<>();

            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                byte[] fileBytes = file.getBytes();

                if (pdfConversionService.isPdfFile(filename)) {
                    pdfBytesList.add(fileBytes);
                } else if (pdfConversionService.isImageFile(filename)) {
                    imageBytesList.add(fileBytes);
                } else if (pdfConversionService.isTextFile(filename)) {
                    // Convert text to PDF and add to PDF list
                    byte[] pdfBytes = pdfConversionService.convertTextToPdf(fileBytes);
                    pdfBytesList.add(pdfBytes);
                } else {
                    throw new RuntimeException("Unsupported file type: " + filename);
                }
            }

            // Convert all images to PDF
            if (!imageBytesList.isEmpty()) {
                byte[] imagePdf = pdfConversionService.mergeImagesToPdf(imageBytesList);
                pdfBytesList.add(imagePdf);
            }

            // Merge all PDFs into one
            byte[] finalPdfBytes;
            if (pdfBytesList.size() == 1) {
                finalPdfBytes = pdfBytesList.get(0);
            } else {
                finalPdfBytes = pdfConversionService.mergePdfs(pdfBytesList);
            }

            // Save merged PDF
            String pdfFileName = UUID.randomUUID().toString() + "_merged.pdf";
            Path pdfPath = Paths.get(UPLOAD_DIR + pdfFileName);
            Files.write(pdfPath, finalPdfBytes);
            
            medicalFile.setPdfUrl(pdfPath.toString());
            medicalFile.setProcessingStatus(FileProcessingStatus.COMPLETED);
        } catch (Exception e) {
            medicalFile.setProcessingStatus(FileProcessingStatus.FAILED);
            medicalFileRepository.save(medicalFile);
            throw new RuntimeException("File processing failed: " + e.getMessage());
        }

        medicalFile = medicalFileRepository.save(medicalFile);
        return mapToResponse(medicalFile);
    }

    private byte[] processFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();

        if (pdfConversionService.isPdfFile(filename)) {
            // Already PDF, return as is
            return fileBytes;
        } else if (pdfConversionService.isImageFile(filename)) {
            // Convert image to PDF
            return pdfConversionService.convertImageToPdf(fileBytes);
        } else if (pdfConversionService.isTextFile(filename)) {
            // Convert text to PDF
            return pdfConversionService.convertTextToPdf(fileBytes);
        } else {
            throw new RuntimeException("Unsupported file type: " + filename);
        }
    }

    public List<MedicalFileResponse> getMedicalFilesByPatient(Long patientId, Long userId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        List<MedicalFile> files = medicalFileRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return files.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MedicalFileResponse getMedicalFileById(Long fileId, Long userId) {
        MedicalFile file = medicalFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Medical file not found"));

        if (!file.getPatient().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(file);
    }

    @Transactional
    public void deleteMedicalFile(Long fileId, Long userId) {
        MedicalFile file = medicalFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Medical file not found"));

        if (!file.getPatient().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Delete PDF file
        try {
            Files.deleteIfExists(Paths.get(file.getPdfUrl()));
        } catch (IOException e) {
            // File deletion failed
        }

        medicalFileRepository.delete(file);
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1) : "";
    }

    private MedicalFileResponse mapToResponse(MedicalFile file) {
        MedicalFileResponse response = new MedicalFileResponse();
        response.setId(file.getId());
        response.setUploadId(file.getUploadId());
        response.setPatientId(file.getPatient().getId());
        response.setPatientName(file.getPatient().getName());
        response.setOriginalFilename(file.getOriginalFilename());
        response.setOriginalFiletype(file.getOriginalFiletype());
        response.setPdfUrl(file.getPdfUrl());
        response.setFileCategory(file.getFileCategory());
        response.setDescription(file.getDescription());
        response.setProcessingStatus(file.getProcessingStatus() != null ? file.getProcessingStatus().name() : null);
        response.setCreatedAt(file.getCreatedAt());
        response.setUpdatedAt(file.getUpdatedAt());
        return response;
    }
}
