package com.app.medivault.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DeviceToken {

    @Id @GeneratedValue
    private Long id;

    private String token;

    @ManyToOne
    private User user;
}
