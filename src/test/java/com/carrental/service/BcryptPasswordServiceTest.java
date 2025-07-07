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
//      create mock data
        CharSequence rawPassword = "kunal@123";
        String hashed = "hashed123";

//      When
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);
        String encodedPassword = bcryptPasswordService.encode(rawPassword);

//      assertions
        assertNotNull(encodedPassword);
        assertEquals(hashed , encodedPassword);
    }

    @Test
    public void shouldMatchRawPasswordWithEncodedPassword(){
//      create mock data
        CharSequence rawPassword = "kunal@123";
        String hashed = "hashed123";

//      when
        when(passwordEncoder.matches(rawPassword , hashed)).thenReturn(true);
        boolean result = bcryptPasswordService.matches(rawPassword , hashed);

//      assertions
        assertTrue(result);

    }

}
