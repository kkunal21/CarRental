package com.carrental.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class BcryptPasswordServiceTest {

    @Inject
    BcryptPasswordService bcryptPasswordService;

    @Test
    public void encode(){
        CharSequence rawPassword = "kunal@123";
        String encodedPassword = bcryptPasswordService.encode(rawPassword);
        assertTrue(bcryptPasswordService.matches((CharSequence) "kunal@123", encodedPassword) );
    }

    @Test
    public void matches(){
        CharSequence rawPassword = "kunal@123";
        String encodedPassword = bcryptPasswordService.encode(rawPassword);
        assertTrue(bcryptPasswordService.matches((CharSequence) "kunal@123", encodedPassword) );
    }

}
