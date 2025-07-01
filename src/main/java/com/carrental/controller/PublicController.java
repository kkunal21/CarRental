package com.carrental.controller;


import com.carrental.dto.LoginRequest;
import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/public")
@Secured((SecurityRule.IS_ANONYMOUS))
public class PublicController {

    private final UserService userService;

    @Inject
    public PublicController(UserService userService){
        this.userService = userService;
    }

    @Post("/userSignUp")
    public HttpResponse<?> createNewUser(@Body LoginRequest loginRequest) {
        userService.createNewUser(loginRequest);
        return HttpResponse.created("User Created");

    }




}
