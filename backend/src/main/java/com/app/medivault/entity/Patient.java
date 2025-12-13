package com.app.medivault.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Patient {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private LocalDate dob;
    private String bloodGroup;

    @ManyToOne
    private User user;
}
