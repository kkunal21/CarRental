package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    AdminService adminService;

    @Mock
    CarRepository carRepository;

    @Test
    public void shouldReturnListOfAllCars_WhenCarsExist(){
        // Create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), true);

        List<Car> mockCars =  listOf(car1 , car2);

        //when
        when(carRepository.findAll()).thenReturn(mockCars);

        List<Car>  carList = adminService.getAllCars();

//      assertions
        assertEquals(2 ,  carList.size());
        assertEquals("BMW" , carList.get(0).getBrand());
        assertEquals("520D" ,carList.get(0).getModel());
        assertEquals(BigDecimal.valueOf(10000) , carList.get(0).getPricePerDay());
        assertEquals("AUDI" , carList.get(1).getBrand());
        assertEquals("R8" ,carList.get(1).getModel());
        assertEquals(BigDecimal.valueOf(12000) , carList.get(1).getPricePerDay());
    }

    @Test
    public void shouldReturnListOfBookedCars_WhenCarsAreBooked(){
        // Create data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), false);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
//        Car car3 = new Car(null, "XUV700" , "MAHINDRA" , BigDecimal.valueOf(7000), true);
        List<Car> mockCars = listOf(car1 , car2 );
//      when
        when(carRepository.findByAvailability(false)).thenReturn(mockCars);

        List<Car> bookedCars =  adminService.getBookedCars();

//      assertions
        assertEquals(2 , bookedCars.size());
        assertFalse(bookedCars.stream().allMatch(Car::isAvailability));
    }

    @Test
    public void shouldReturnListOfAvailableCars_WhenCarsAreAvailable(){
//      create mock data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        List<Car> mockCars = listOf(car1 , car2 );

//      when
        when(carRepository.findByAvailability(true)).thenReturn(mockCars);

        List<Car> availableCars =  adminService.getAvailableCars();
//      assertions
        assertEquals(2 , availableCars.size());
        assertTrue(availableCars.stream().allMatch(Car::isAvailability));
    }

    @Test
    public void shouldAddCarSuccessfully_WhenCarDataIsValid(){
//     crete mock data
       Car car1 = new Car(null, "XUV700" , "MAHINDRA" , BigDecimal.valueOf(7000), true);

//     when
       adminService.addCar(car1);

//       assertions
        verify(carRepository , times(1)).save(car1);

    }

    @Test
    public void shouldReturnCar_WhenCarIdIsValid(){
//      create mock data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);

//      when
        when(carRepository.findById(car1.getId())).thenReturn(Optional.of(car1));

        Optional<Car> result = adminService.findById(car1.getId());
//      assertions
        assertTrue(result.isPresent());
        assertEquals("BMW" , result.get().getBrand());
        assertEquals("520D" , result.get().getModel());
    }

    @Test
    public void shouldThrowException_WhenCarIdDoesNotExist(){
//      create mock data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);

//      when
        when(carRepository.findById(car1.getId())).thenReturn(Optional.empty());

        Optional<Car> result = adminService.findById(car1.getId());

//      assertions
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldUpdateCarDetailsSuccessfully_WhenCarExists(){
//      create Mock Data
        Car existingCar= new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car updatedInput = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Car updatedCar = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Long carId = 1L;

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.update(any(Car.class))).thenReturn(updatedCar);

        Car latestCar = adminService.updateCarDetails(carId , updatedInput);

//      assertions
        assertEquals("R8" , latestCar.getModel());
        assertEquals("AUDI" , latestCar.getBrand());
        assertEquals(BigDecimal.valueOf(12000) , latestCar.getPricePerDay());
        assertFalse(latestCar.isAvailability());

        verify(carRepository , times(1)).findById(carId);
        verify(carRepository , times(1)).update(any(Car.class));
    }

    @Test
    public void shouldFailToUpdateCar_WhenCarIdDoesNotExist(){
//        Create Mock data
        Long carId = 11L;
        Car updatedInput = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.empty());


//      assertions
        assertThrows(NoSuchElementException.class, () -> {
            adminService.updateCarDetails(carId, updatedInput);
        });
        verify(carRepository , times(1)).findById(carId);
        verify(carRepository, never()).save(any(Car.class));

    }

    @Test
    public void shouldDeleteCarSuccessfully_WhenCarIdIsValid(){
//      create mock data
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car1));

        adminService.deleteCar(carId);

//      assertions
        verify(carRepository , times(1)).deleteById(carId);
    }

    @Test
    public void shouldFailToDeleteCar_WhenCarIdDoesNotExist(){
//      create Mock data
        Long carId = 2L;

//      assertions
        assertThrows(NoSuchElementException.class, () -> {
            adminService.deleteCar(carId);
        });
        verify(carRepository, never()).deleteById(any());
    }

}
