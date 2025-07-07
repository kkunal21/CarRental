package com.carrental.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Singleton
@Serdeable
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    String username;
    @NotBlank
    String Password;

}
