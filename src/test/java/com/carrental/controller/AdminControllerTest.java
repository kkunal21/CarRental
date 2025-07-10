package com.carrental.controller;

import com.carrental.dto.LoginRequest;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.AdminService;
import com.carrental.service.BcryptPasswordService;
import com.carrental.service.UserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MicronautTest(transactional = false)
public class AdminControllerTest {

    @Inject
     AdminService adminService;

    @Inject
    UserService userService;

    @Inject
    BcryptPasswordService passwordEncoder;

    @Inject
    UserRepository userRepository;

    @Inject
    CarRepository carRepository;

    @Inject
    @Client("/")
    HttpClient client;

    String authToken;


//    Setup Admin Profile in order to Access Secured EndPoints
    @BeforeAll
    static void setupAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.findByUserName("mainAdmin").isEmpty()) {
            User user = new User(
                    "mainAdmin",
                    passwordEncoder.encode("admin@123"),
                    "ADMIN");
            userRepository.save(user);
        }
    }

    @BeforeEach
    void setup() {
        // Delete all users except the admin (e.g. "mainAdmin")
        userRepository.findAll().stream()
                .filter(user -> !user.getUserName().equals("mainAdmin"))
                .forEach(user -> userRepository.deleteById(user.getId()));
        carRepository.deleteAll();
    }


    //   login As Admin To get Authorisation Token,needed to access secured endpoint
    @BeforeEach
    public void logIn(){

       try {
//           request
          HttpRequest<LoginRequest> loginRequest = HttpRequest.POST(
                  "/login",
                  new LoginRequest("mainAdmin", "admin@123"));

//           response
           HttpResponse<BearerAccessRefreshToken> tokenHttpResponse =
                   client
                   .toBlocking()
                   .exchange(loginRequest, BearerAccessRefreshToken.class);

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
    public void shouldCreateNewAdminSuccessfully_whenDataIsValid(){
//        request
        HttpRequest<LoginRequest> loginRequest = HttpRequest.POST
                        ("/admin/admin-sign-up", new LoginRequest("Max", "Max@123"))
                .bearerAuth(authToken)
                .contentType(MediaType.APPLICATION_JSON);

//       response
        HttpResponse<String> response =
                client
                        .toBlocking()
                        .exchange(loginRequest, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("Admin Created Successfully", response.getBody().get());

    }
    @Test
    public void createAdmin_ShouldFail_WhenAdminAlreadyExists(){
//        request
        HttpRequest<LoginRequest> loginRequest =
                HttpRequest.POST
                        ("/admin/admin-sign-up", new LoginRequest("Max", "Max@123"))
                        .bearerAuth(authToken)
                        .contentType(MediaType.APPLICATION_JSON);
        try{
//            response
                    client
                            .toBlocking()
                            .exchange(loginRequest , String.class);

        }catch(HttpClientResponseException e){
            JsonError error  = e.getResponse().getBody(JsonError.class).orElse(null);
            assertNotNull(error);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
            assertEquals("Admin Already Exist , Try singing in..." , error.getMessage());
        }
    }


    @Test
    public void createAdmin_ShouldFail_WhenUsernameOrPasswordIsMissing(){
//        request
        HttpRequest<LoginRequest> loginRequest =
                HttpRequest.POST
                        ("/admin/admin-sign-up", new LoginRequest("", "Max@123"))
                        .bearerAuth(authToken)
                        .contentType(MediaType.APPLICATION_JSON);
        try{
//            response
            client
                    .toBlocking()
                    .exchange(loginRequest , String.class);

        }catch(HttpClientResponseException e){
            JsonError error  = e.getResponse().getBody(JsonError.class).orElse(null);
            assertNotNull(error);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
            assertEquals("Username or Password cannot be blank or empty" , error.getMessage());
        }
    }

    @Test
    public void findAllCars_ShouldReturnListOfAllCars(){
//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        adminService.addCar(car1);
        adminService.addCar(car2);

//        request
        HttpRequest<Object> request = HttpRequest.GET
                ("/admin/all-cars" ).
                bearerAuth(authToken);
//        response
        HttpResponse<List> response =
                client.
                toBlocking().
                exchange(request ,  Argument.of(List.class, Car.class));

        List<Car> cars = response.getBody().orElse(Collections.EMPTY_LIST);
        assertEquals(2 , cars.size());
        assertEquals(HttpStatus.OK , response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("BMW" , cars.getFirst().getBrand());
        assertEquals("AUDI" , cars.get(1).getBrand());
    }

    @Test
    public void shouldReturnListOfBookedCars_WhenCarsAreBooked(){

//       create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false);

        carRepository.save(car1);
        carRepository.save(car2);

//        request
        HttpRequest<Object> request = HttpRequest.GET
                ("/admin/booked-cars" )
                .bearerAuth(authToken);

//       response
        HttpResponse<List> response =
                client
                .toBlocking()
                .exchange(request ,  Argument.of(List.class, Car.class));

        List<Car> bookedCars = response.getBody().orElse(Collections.EMPTY_LIST);
        assertEquals(2 , bookedCars.size());
        assertEquals(HttpStatus.OK , response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("BMW" , bookedCars.getFirst().getBrand());
        assertEquals("AUDI" , bookedCars.get(1).getBrand());
    }


    @Test
    public void shouldReturnListOfAvailableCars_WhenCarsAreAvailable(){

//        request
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Car car3 = new Car(null, "Q7" , "AUDI-Q" , BigDecimal.valueOf(12000), true);

        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);

//        request
        HttpRequest<Object> request = HttpRequest.GET
                ("/admin/available-cars" )
                .bearerAuth(authToken);

//        response
        HttpResponse<List> response =
                client
                .toBlocking()
                .exchange(request ,  Argument.of(List.class, Car.class));

        List<Car> availableCars = response.getBody().orElse(Collections.EMPTY_LIST);
        assertEquals(1 , availableCars.size());
        assertEquals(HttpStatus.OK , response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("AUDI-Q" , availableCars.getFirst().getBrand());

    }

    @Test
    public void findCarById_shouldReturnCar_WhenCarExists(){

//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        adminService.addCar(car1);
        Long carId = car1.getId();

//        request
        HttpRequest<Object> request =
                HttpRequest.GET
                        ("/admin/find-car/" + carId)
                        .bearerAuth(authToken);

//        response
        HttpResponse<Car> response =
                client
                        .toBlocking()
                        .exchange(request , Car.class);

        assertTrue(response.getBody().isPresent());
        Car fetchedCar = response.getBody().get();
        assertEquals(HttpStatus.OK , response.getStatus());
        assertEquals("520D" , fetchedCar.getModel());
        assertEquals("BMW" , fetchedCar.getBrand());
    }

    @Test
    public void findCarById_shouldThrowException_WhenCarDoesNotExists(){
//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        adminService.addCar(car1);
        Long carId = 2L;

//       request
        HttpRequest<Object> request =
                HttpRequest.GET
                ("/admin/find-car/" + carId)
                .bearerAuth(authToken);

      try{
//          response
         client
                 .toBlocking()
                 .exchange(request , Car.class);

      }catch(HttpClientResponseException e){
          JsonError error = e.getResponse().getBody(JsonError.class).orElse(null);
          assertNull(error);
          assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
      }

    }

    @Test
    public void addCarSuccessfully_WhenDataIsValid(){

//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);

//        request
        HttpRequest<Car> request = HttpRequest.POST
                ("/admin/add-car"  ,car1)
                .bearerAuth(authToken);
//        response
        HttpResponse<Car> response = client
                .toBlocking()
                .exchange(request , Car.class);


        assertTrue(response.getBody().isPresent());
        Car addedCar = response.getBody().get();
        assertEquals(HttpStatus.CREATED , response.getStatus());
        assertEquals("520D" , addedCar.getModel());
        assertEquals("BMW" , addedCar.getBrand());
        assertEquals(BigDecimal.valueOf(10000) , addedCar.getPricePerDay());
    }

    @Test
    public void addCar_FailsToAddCar_WhenDataIsInValid(){

//        create data
        Car car1 = new Car( null ," " , " " , BigDecimal.valueOf(10000), true);

//        request
        HttpRequest<Car> request = HttpRequest.POST("/admin/add-car"  ,car1).bearerAuth(authToken);

        try{
//            response
            client
                    .toBlocking()
                    .exchange(request , Car.class);

        }catch(HttpClientResponseException e){
            JsonError error = e.getResponse().getBody(JsonError.class).orElse(null);
            assertNotNull(error);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());

        }
    }

    @Test
    public void updateCar_Successfully_WhenDataIsValid(){

//        create data
        Car existingCar = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car updatedInput = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), true);

        Car savedCar = carRepository.save(existingCar);
        Long carId = savedCar.getId();


//        request
        HttpRequest<Car> request =
                HttpRequest.PUT
                        ("/admin/update-car/" + carId , updatedInput)
                        .bearerAuth(authToken);


//        response
        HttpResponse<Car> response =
                client
                        .toBlocking()
                        .exchange(request , Car.class);

        assertTrue(response.getBody().isPresent());
        Car newCar = response.getBody().get();
        assertEquals(HttpStatus.OK ,response.getStatus() );
        assertEquals("R8" , newCar.getModel());
        assertEquals("AUDI" , newCar.getBrand());
        assertEquals(BigDecimal.valueOf(12000) , newCar.getPricePerDay());
    }

    @Test
    public void updateCar_ShouldFail_WhenDataIsInValid(){

//        create data
        Car existingCar = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car updatedInput = new Car(null , "" , "AUDI" , BigDecimal.valueOf(12000), true);

        Car savedCar = carRepository.save(existingCar);
        Long carId = savedCar.getId();

//        request
        HttpRequest<Car> request =
                HttpRequest.PUT
                        ("/admin/update-car/" + carId , updatedInput)
                        .bearerAuth(authToken);

        try{
//            response
            HttpResponse<Car> response =
                    client
                            .toBlocking()
                            .exchange(request , Car.class);

        }catch(HttpClientResponseException e){
            JsonError error = e.getResponse().getBody(JsonError.class).orElse(null);
            assertNotNull(error);
            assertEquals(HttpStatus.BAD_REQUEST , e.getStatus());
        }
    }

    @Test
    public void deleteCar_ShouldDeleteCar_IfIdExists(){

//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car savedCar = carRepository.save(car1);
        Long carId = savedCar.getId();

//        request
        HttpRequest<Object> request =
                HttpRequest.DELETE
                        ("/admin/delete-car/" + carId)
                        .bearerAuth(authToken);

//        response
        HttpResponse<String> response =
                client
                        .toBlocking()
                        .exchange(request ,String.class);

        assertEquals(HttpStatus.OK , response.getStatus());
        assertTrue(response.getBody().isPresent());
        assertEquals("Car Deleted" , response.getBody().get());
    }

    @Test
    public void deleteCar_ShouldFail_IfIdDoesNotExists(){

//        create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
         carRepository.save(car1);
        Long carId = 2L;


//        request
        HttpRequest<Object> request =
                HttpRequest.DELETE
                        ("/admin/delete-car/" + carId)
                        .bearerAuth(authToken);

        try{
//            response
            client
                    .toBlocking()
                    .exchange(request ,String.class);

        }catch(HttpClientResponseException e){
           assertEquals(HttpStatus.NOT_FOUND, e.getStatus());

        }
    }





}
