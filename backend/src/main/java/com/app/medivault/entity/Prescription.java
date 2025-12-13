package com.app.medivault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "uploadId"))
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String uploadId;

    private String imageUrl;

    @ManyToOne
    private Patient patient;
}
