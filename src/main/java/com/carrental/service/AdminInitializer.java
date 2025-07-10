package com.carrental.service;

import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import io.micronaut.context.annotation.Context;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Context
public class AdminInitializer {

    private final UserRepository userRepository;
    private final BcryptPasswordService passwordService;

    @Inject
    public AdminInitializer(UserRepository userRepository , BcryptPasswordService passwordService){
        System.out.println("✅ AdminInitializer constructor called");
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @PostConstruct
    public void adminInit(){

        userRepository.findByUserName("admin").ifPresentOrElse(
                user -> {System.out.println( "Admin already exists");
                } ,
                () -> {
                    User admin = new User();
                    admin.setUserName("admin");
                    admin.setPassword(passwordService.encode("admin@123"));
                    admin.setRole("ADMIN");
                    userRepository.save(admin);
                    System.out.println("Admin is created , please login");
                }
                );
    }

}
