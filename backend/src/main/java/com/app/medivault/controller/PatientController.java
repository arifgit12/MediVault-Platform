package com.app.medivault.controller;

import com.app.medivault.dtos.ApiResponse;
import com.app.medivault.dtos.PatientRequest;
import com.app.medivault.dtos.PatientResponse;
import com.app.medivault.service.PatientService;
import com.app.medivault.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody PatientRequest request) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PatientResponse response = patientService.createPatient(userId, request);
            return ResponseEntity.ok(ApiResponse.success("Patient created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<?> updatePatient(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long patientId,
                                           @RequestBody PatientRequest request) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PatientResponse response = patientService.updatePatient(patientId, userId, request);
            return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPatients(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            List<PatientResponse> patients = patientService.getPatientsByUser(userId);
            return ResponseEntity.ok(ApiResponse.success("Patients retrieved successfully", patients));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> getPatient(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable Long patientId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            PatientResponse patient = patientService.getPatientById(patientId, userId);
            return ResponseEntity.ok(ApiResponse.success("Patient retrieved successfully", patient));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable Long patientId) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            patientService.deletePatient(patientId, userId);
            return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}

