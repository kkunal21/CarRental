package com.carrental.Entity;

import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Singleton
public class Booking {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotNull
    long userId;
    long billAmount;
    long no_of_days;
}
