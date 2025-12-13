package com.app.medivault.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalTime;

@Entity
public class MedicationReminder {

    @Id
    @GeneratedValue
    private Long id;

    private String medicine;
    private LocalTime time;

    @ManyToOne
    private Patient patient;
}
