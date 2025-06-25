package com.carrental.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Introspected
@Serdeable
@AllArgsConstructor
public class BookCarRequest {

    @NotNull
    private  Long carId;
    @NotNull
    private  Long userId;
    @NotNull
    private  Long noOfDays;

}
