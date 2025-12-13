package com.app.medivault.service;

import com.app.medivault.entity.Medicine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PrescriptionTextParser {

    public List<Medicine> parseMedicines(String text) {

        List<Medicine> medicines = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "(?i)(Paracetamol|Amoxicillin|Ibuprofen)\\s*(\\d+mg)?\\s*(.*)"
        );

        for (String line : text.split("\n")) {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                Medicine med = new Medicine();
                med.setName(m.group(1));
                med.setDosage(m.group(2));
                med.setFrequency("As prescribed");
                med.setDuration("N/A");
                medicines.add(med);
            }
        }
        return medicines;
    }
}

