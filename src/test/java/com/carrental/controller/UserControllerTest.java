package com.carrental.controller;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.dto.LoginRequest;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import io.micronaut.core.type.Argument;
import io.micronaut.http.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class UserControllerTest {

    @Inject
    UserService userService;

    @Inject
    BookingService bookingService;

    @Inject
    CarRepository carRepository;

    @Inject
    @Client("/")
    HttpClient client;

    String authToken;

    @BeforeAll
    static void setupAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.findByUserName("kunal").isEmpty()) {
            User user1 = new User("kunal", passwordEncoder.encode("kunal@123"), "USER");
            userRepository.save(user1);
        }
    }

    @BeforeEach
    public void logIn(){

        try {
            HttpRequest<LoginRequest> loginRequest = HttpRequest.POST("/login", new LoginRequest("kunal", "kunal@123"));
            HttpResponse<BearerAccessRefreshToken> tokenHttpResponse = client.toBlocking().exchange(loginRequest, BearerAccessRefreshToken.class);

            assertTrue(tokenHttpResponse.getBody().isPresent());
            assertNotNull(tokenHttpResponse.getBody().get().getAccessToken());

            BearerAccessRefreshToken accessRefreshToken = tokenHttpResponse.getBody().get();
            authToken = (String) accessRefreshToken.getAccessToken();
        }catch(HttpClientResponseException e)
        {
            System.out.println("Request failed with status: " + e.getStatus());
            System.out.println("ResponseBody :" + e.getResponse().getBody(String.class).orElse("No body"));
            throw e;
        }
    }


    @Test
    public void availableCars_ShouldReturnListOfAvailableCars(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Car car3 = new Car(null, "RR" , "LANDROVER" , BigDecimal.valueOf(12000), true);
        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);

        HttpRequest<Object> request = HttpRequest.GET("/user/available-cars" ).bearerAuth(authToken);
        HttpResponse<List> response = client.toBlocking().exchange(request , Argument.of(List.class , Car.class));
        assertTrue(response.getBody().isPresent());

        List<Car> carList = response.getBody().orElse(Collections.EMPTY_LIST);

        assertEquals(2 , carList.size());
        assertEquals(HttpStatus.OK , response.getStatus());
        assertEquals("520D" ,carList.getFirst().getModel());
        assertEquals( "RR", carList.get(1).getModel());
    }

    @Test
    public void bookCar_ShouldBookCarWhenRequestIsValid(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car savedCar = carRepository.save(car1);
        Long carId = savedCar.getId();
        BookCarRequest bookCarRequest = new BookCarRequest(carId , 1L , 9L);

        HttpRequest<BookCarRequest> request = HttpRequest.POST("/user/book-car" , bookCarRequest).bearerAuth(authToken).contentType(MediaType.APPLICATION_JSON);;

        HttpResponse<BookCarResponse> response = client.toBlocking().exchange(request , BookCarResponse.class);

        assertTrue(response.getBody().isPresent());
        BookCarResponse responseBody = response.getBody().get();
        assertEquals(HttpStatus.CREATED ,response.getStatus());
        assertEquals(0, BigDecimal.valueOf(90000).compareTo(responseBody.getBillAmount()));

        assertEquals("Car Booked Successfully" , responseBody.getMessage());

    }

    @Test
    public void bookCar_ShouldFailToBookCarWhenCarIdDoesNotExist(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
         carRepository.save(car1);
        Long carId = 3L;
        BookCarRequest bookCarRequest = new BookCarRequest(carId , 1L , 9L);

        HttpRequest<BookCarRequest> request = HttpRequest.POST("/user/book-car" , bookCarRequest).bearerAuth(authToken).contentType(MediaType.APPLICATION_JSON);;
        try{
            client.toBlocking().exchange(request , BookCarResponse.class);
        }catch(HttpClientResponseException e){
//            JsonError error = e.getResponse().getBody(JsonError.class).orElse(null);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
        }
    }

    @Test
    public void bookCar_ShouldFailToBookCarWhenCarIsNotAvailable(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
         carRepository.save(car1);
        Long carId = 1L;
        BookCarRequest bookCarRequest = new BookCarRequest(carId , 1L , 9L);

        HttpRequest<BookCarRequest> request = HttpRequest.POST("/user/book-car" , bookCarRequest).bearerAuth(authToken).contentType(MediaType.APPLICATION_JSON);;
        try{
            client.toBlocking().exchange(request , BookCarResponse.class);
        }catch(HttpClientResponseException e){
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
        }
    }

    @Test
    public void returnCar_shouldSuccessfullyReturnCar(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
        carRepository.save(car1);
        Long carId = car1.getId();

        HttpRequest<Object> request = HttpRequest.POST("/user/return-car/" + carId, null)
                .bearerAuth(authToken);
        HttpResponse<String> response = client.toBlocking().exchange(request ,String.class);

        assertTrue(response.getBody().isPresent());
        assertEquals(HttpStatus.OK , response.getStatus());
        assertEquals("Car Returned Successfully" , response.getBody().get());

    }
    @Test
    public void returnCar_shouldThrowExceptionWhenCarIsNotBooked(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
        carRepository.save(car1);
        Long carId = car1.getId();

        HttpRequest<Object> request = HttpRequest.POST("/user/return-car/" + carId, null)
                .bearerAuth(authToken);
        try{
            client.toBlocking().exchange(request ,String.class);
        }catch(HttpClientResponseException e)
        {
            JsonError error = e.getResponse().getBody(JsonError.class).orElse(null);
            assertNotNull(error);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
            assertEquals("This Car was not booked so it cannot be returned" , e.getMessage());
        }
    }




}
