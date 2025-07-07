package com.carrental.controller;

import com.carrental.dto.LoginRequest;
import com.carrental.service.UserService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class PublicControllerTest {

    @Inject
    UserService userService;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void shouldCreateNewUserSuccessfully_whenDataIsValid(){

        HttpRequest<LoginRequest> request =
                HttpRequest.POST
                        ("/public/user-sign-up" , new LoginRequest("kunal" , "kunal@123"));

        HttpResponse<String> response =
                client
                        .toBlocking()
                        .exchange(request , String.class);

        assertEquals(HttpStatus.CREATED , response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("User Created"  , response.getBody().get());
    }

}
