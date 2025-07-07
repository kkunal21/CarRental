package com.carrental.service;

import jakarta.inject.Singleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
public class BcryptPasswordService implements PasswordEncoder {

    private final PasswordEncoder passwordEncoder ;

    public BcryptPasswordService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

//    encodes the password via bCryptPasswordEncoder
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

//  checks if the raw password from user matches the encoded password in DB
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
