package com.app.medivault.controller;

import com.app.medivault.dtos.ApiResponse;
import com.app.medivault.dtos.PrescriptionRequest;
import com.app.medivault.dtos.PrescriptionResponse;
import com.app.medivault.service.PrescriptionService;
import com.app.medivault.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPrescription(@RequestHeader("Authorization") String authHeader,
                                                @RequestParam("uploadId") String uploadId,
                                                @RequestParam("patientId") Long patientId,
                                                @RequestParam("file") MultipartFile file) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PrescriptionResponse response = prescriptionService.uploadPrescription(uploadId, patientId, file, userId);
            return ResponseEntity.ok(ApiResponse.success("Prescription uploaded successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{prescriptionId}")
    public ResponseEntity<?> updatePrescription(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable Long prescriptionId,
                                                @RequestBody PrescriptionRequest request) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PrescriptionResponse response = prescriptionService.updatePrescription(prescriptionId, request, userId);
            return ResponseEntity.ok(ApiResponse.success("Prescription updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPrescriptionsByPatient(@RequestHeader("Authorization") String authHeader,
                                                       @PathVariable Long patientId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            List<PrescriptionResponse> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId, userId);
            return ResponseEntity.ok(ApiResponse.success("Prescriptions retrieved successfully", prescriptions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{prescriptionId}")
    public ResponseEntity<?> getPrescription(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable Long prescriptionId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PrescriptionResponse prescription = prescriptionService.getPrescriptionById(prescriptionId, userId);
            return ResponseEntity.ok(ApiResponse.success("Prescription retrieved successfully", prescription));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<?> deletePrescription(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable Long prescriptionId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            prescriptionService.deletePrescription(prescriptionId, userId);
            return ResponseEntity.ok(ApiResponse.success("Prescription deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}

