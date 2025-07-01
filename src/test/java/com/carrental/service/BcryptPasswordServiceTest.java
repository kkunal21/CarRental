package com.carrental.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BcryptPasswordServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    BcryptPasswordService bcryptPasswordService;



    @Test
    public void shouldEncodePasswordUsingBCrypt(){
        CharSequence rawPassword = "kunal@123";
        String hashed = "hashed123";
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);
        String encodedPassword = bcryptPasswordService.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertEquals(hashed , encodedPassword);
    }

    @Test
    public void shouldMatchRawPasswordWithEncodedPassword(){
        CharSequence rawPassword = "kunal@123";
        String hashed = "hashed123";
        when(passwordEncoder.matches(rawPassword , hashed)).thenReturn(true);
        boolean result = bcryptPasswordService.matches(rawPassword , hashed);
        assertTrue(result);

    }

}
