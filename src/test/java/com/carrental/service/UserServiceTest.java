package com.carrental.service;

import com.carrental.dto.LoginRequest;
import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BcryptPasswordService bcryptPasswordService;



    @Test
    public void shouldCreateUserSuccessfully() {
//       create mock data
        LoginRequest loginRequest = new LoginRequest("kunal", "kunal@123");
        String encodedPassword = "hashedppassword123";

//      when
        when(bcryptPasswordService.encode("kunal@123")).thenReturn(encodedPassword);
        when(userRepository.findByUserName("kunal")).thenReturn(Optional.empty());

        userService.createNewUser(loginRequest);
        User expectedUser = new User("kunal", encodedPassword, "USER");

//      assertions
        verify(userRepository, times(1)).save(argThat(savedUser ->
                savedUser.getUserName().equals(expectedUser.getUserName()) &&
                        savedUser.getPassword().equals(expectedUser.getPassword()) &&
                        savedUser.getRole().equals(expectedUser.getRole())
        ));
    }


    @Test
    public void shouldFailToCreateUser_WhenUserAlreadyExists() {

//      create mock data
        User existingUser = new User("kunal", "hashedPassword", "USER");
        LoginRequest loginRequest = new LoginRequest("kunal", "kunal@333");

//      when
        when(userRepository.findByUserName("kunal")).thenReturn(Optional.of(existingUser));

//      assertions
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.createNewUser(loginRequest));
        assertEquals("User Already Exist , Try singing in...", exception.getMessage());
        verify(userRepository, times(1)).findByUserName("kunal");
    }



    @Test
    public void shouldCreateAdminSuccessfully() {
//      create mock data
        LoginRequest loginRequest = new LoginRequest("kunal", "kunal@123");
        String encodedPassword = "hashedppassword123";

//      when
        when(bcryptPasswordService.encode("kunal@123")).thenReturn(encodedPassword);
        when(userRepository.findByUserName("kunal")).thenReturn(Optional.empty());
        userService.createNewAdmin(loginRequest);
        User expectedUser = new User("kunal", encodedPassword, "ADMIN");

//      assertions
        verify(userRepository, times(1)).save(argThat(savedUser ->
                savedUser.getUserName().equals(expectedUser.getUserName()) &&
                        savedUser.getPassword().equals(expectedUser.getPassword()) &&
                        savedUser.getRole().equals(expectedUser.getRole())
        ));
    }



    @Test
    public void shouldFailToCreateAdmin_WhenAdminAlreadyExists(){

//      create mock data
        User user1 = new User( "kunal" , "kunal@123" , "USER");
        LoginRequest request = new LoginRequest( "kunal" , "kunal@123" );
        String userName = request.getUsername();

//      when
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user1));

//      assertions
        assertThrows(RuntimeException.class , ()->
                userService.createNewUser(request) , "User Already Exist , Try singing in...");
//        assertEquals("User Already Exist , Try singing in..." , exception.getMessage());
        verify(userRepository , times(1)).findByUserName(userName);
    }




}
