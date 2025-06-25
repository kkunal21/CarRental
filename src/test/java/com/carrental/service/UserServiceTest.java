package com.carrental.service;

import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Test
    public void createNewUserTest(){

        User user1 = new User(null , "kunal" , "kunal@123" , "USER");
        userService.createNewUser(user1);

        Optional<User> optionalUser= userRepository.findById(user1.getId());

        optionalUser.ifPresent(user -> assertEquals(user , optionalUser.get()));
        assertEquals("USER" , optionalUser.get().getRole());

    }

    @Test
    public void createNewAdminTest(){

        User user1 = new User(null , "kunal" , "kunal@123" , "ADMIN");
        userService.createNewAdmin(user1);

        Optional<User> optionalUser= userRepository.findById(user1.getId());

        optionalUser.ifPresent(user -> assertEquals(user , optionalUser.get()));
        assertEquals("ADMIN" , optionalUser.get().getRole());

    }



}
