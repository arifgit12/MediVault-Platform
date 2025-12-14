package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String name;
    private LocalDate dob;
    private String gender; // MALE, FEMALE, OTHER
    private String bloodGroup;
    private String allergies;
    private String relationship;
}
