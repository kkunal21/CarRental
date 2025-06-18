package com.carrental.entity;

import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Singleton
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long carId;
    @NotNull
    private Long userId;
    private BigDecimal billAmount;
    private Long no_of_days;
}
