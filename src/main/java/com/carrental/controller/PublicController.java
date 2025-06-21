package com.carrental.controller;


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

    @Post("/addUser")
    public HttpResponse<?> createNewUser(@Body User user){
        userService.createUser(user);
        return HttpResponse.created("User Created");
    }


}
