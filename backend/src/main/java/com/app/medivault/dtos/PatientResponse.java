package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String name;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;
    private String allergies;
    private String relationship;
    private Integer age;
    private LocalDateTime createdAt;
}
