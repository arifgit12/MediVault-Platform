package com.app.medivault.repository;

import com.app.medivault.entity.MedicalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalFileRepository extends JpaRepository<MedicalFile, Long> {
    Optional<MedicalFile> findByUploadId(String uploadId);
    List<MedicalFile> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
