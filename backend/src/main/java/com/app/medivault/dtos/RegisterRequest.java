package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String role; // PATIENT, DOCTOR, CLINIC_ADMIN
}
