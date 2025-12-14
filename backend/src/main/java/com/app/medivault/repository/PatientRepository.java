package com.app.medivault.repository;

import com.app.medivault.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByUserId(Long userId);
    List<Patient> findByUserIdOrderByCreatedAtDesc(Long userId);
}
