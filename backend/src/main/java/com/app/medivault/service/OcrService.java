package com.app.medivault.service;

import com.app.medivault.entity.Medicine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcrService {

    public List<Medicine> extract(String imagePath) {
        return List.of(new Medicine("Paracetamol", "500mg", "2x/day", "5 days"));
    }
}
