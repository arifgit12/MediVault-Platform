package com.app.medivault.repository;

import com.app.medivault.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByUserId(Long userId);
}
