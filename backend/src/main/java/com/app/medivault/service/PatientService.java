package com.app.medivault.service;

import com.app.medivault.dtos.PatientRequest;
import com.app.medivault.dtos.PatientResponse;
import com.app.medivault.entity.Gender;
import com.app.medivault.entity.Patient;
import com.app.medivault.entity.User;
import com.app.medivault.repository.PatientRepository;
import com.app.medivault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PatientResponse createPatient(Long userId, PatientRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setDob(request.getDob());
        
        try {
            patient.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        } catch (Exception e) {
            patient.setGender(null);
        }
        
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setRelationship(request.getRelationship());
        patient.setUser(user);

        patient = patientRepository.save(patient);

        return mapToResponse(patient);
    }

    @Transactional
    public PatientResponse updatePatient(Long patientId, Long userId, PatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Verify ownership
        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        patient.setName(request.getName());
        patient.setDob(request.getDob());
        
        try {
            patient.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        } catch (Exception e) {
            // Keep existing gender if invalid
        }
        
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setRelationship(request.getRelationship());

        patient = patientRepository.save(patient);

        return mapToResponse(patient);
    }

    public List<PatientResponse> getPatientsByUser(Long userId) {
        List<Patient> patients = patientRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return patients.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(Long patientId, Long userId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Verify ownership
        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(patient);
    }

    @Transactional
    public void deletePatient(Long patientId, Long userId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Verify ownership
        if (!patient.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        patientRepository.delete(patient);
    }

    private PatientResponse mapToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setDob(patient.getDob());
        response.setGender(patient.getGender() != null ? patient.getGender().name() : null);
        response.setBloodGroup(patient.getBloodGroup());
        response.setAllergies(patient.getAllergies());
        response.setRelationship(patient.getRelationship());
        response.setAge(patient.getAge());
        response.setCreatedAt(patient.getCreatedAt());
        return response;
    }
}
