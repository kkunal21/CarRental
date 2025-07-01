package com.carrental.dto;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Singleton
@AllArgsConstructor
public class LoginRequest {

    String userName;
    String Password;
}
