package com.carrental.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Singleton
@Serdeable
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    String userName;
    String password;
    List<String> roles;

}
