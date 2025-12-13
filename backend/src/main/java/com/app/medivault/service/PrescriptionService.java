package com.app.medivault.service;

import com.app.medivault.entity.Medicine;
import com.app.medivault.entity.Prescription;
import com.app.medivault.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PrescriptionService {

    private final GoogleVisionOcrService ocrService;
    private final PrescriptionTextParser parser;
    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(GoogleVisionOcrService ocrService,
                               PrescriptionTextParser parser,
                               PrescriptionRepository prescriptionRepository) {
        this.ocrService = ocrService;
        this.parser = parser;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription process(MultipartFile file, Long patientId) throws IOException {

        String rawText = ocrService.extractText(file.getBytes());
        List<Medicine> meds = parser.parseMedicines(rawText);

        Prescription p = new Prescription();

        return prescriptionRepository.save(p);
    }
}

