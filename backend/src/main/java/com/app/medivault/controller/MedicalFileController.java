package com.app.medivault.controller;

import com.app.medivault.dtos.ApiResponse;
import com.app.medivault.dtos.MedicalFileResponse;
import com.app.medivault.service.MedicalFileService;
import com.app.medivault.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medical-files")
@CrossOrigin(origins = "*")
public class MedicalFileController {

    @Autowired
    private MedicalFileService medicalFileService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Upload a single medical file (PDF, text, or image)
     * Text and images will be converted to PDF automatically
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedicalFile(@RequestHeader("Authorization") String authHeader,
                                               @RequestParam("uploadId") String uploadId,
                                               @RequestParam("patientId") Long patientId,
                                               @RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "category", required = false) String category,
                                               @RequestParam(value = "description", required = false) String description) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            MedicalFileResponse response = medicalFileService.uploadMedicalFile(uploadId, patientId, file, category, description, userId);
            return ResponseEntity.ok(ApiResponse.success("Medical file uploaded and processed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Upload multiple medical files
     * All files will be merged into a single PDF
     * - Images will be converted to PDF pages
     * - Text files will be converted to PDF pages
     * - PDFs will be merged together
     */
    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleMedicalFiles(@RequestHeader("Authorization") String authHeader,
                                                        @RequestParam("uploadId") String uploadId,
                                                        @RequestParam("patientId") Long patientId,
                                                        @RequestParam("files") List<MultipartFile> files,
                                                        @RequestParam(value = "category", required = false) String category,
                                                        @RequestParam(value = "description", required = false) String description) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            MedicalFileResponse response = medicalFileService.uploadMultipleMedicalFiles(uploadId, patientId, files, category, description, userId);
            return ResponseEntity.ok(ApiResponse.success("Medical files uploaded and merged successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all medical files for a patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getMedicalFilesByPatient(@RequestHeader("Authorization") String authHeader,
                                                      @PathVariable Long patientId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            List<MedicalFileResponse> files = medicalFileService.getMedicalFilesByPatient(patientId, userId);
            return ResponseEntity.ok(ApiResponse.success("Medical files retrieved successfully", files));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get a specific medical file
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<?> getMedicalFile(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long fileId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            MedicalFileResponse file = medicalFileService.getMedicalFileById(fileId, userId);
            return ResponseEntity.ok(ApiResponse.success("Medical file retrieved successfully", file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete a medical file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteMedicalFile(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long fileId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            medicalFileService.deleteMedicalFile(fileId, userId);
            return ResponseEntity.ok(ApiResponse.success("Medical file deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}
