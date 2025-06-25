package com.carrental.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Data
@Singleton
@Serdeable
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    String userName;
    String password;
    String role;

}
