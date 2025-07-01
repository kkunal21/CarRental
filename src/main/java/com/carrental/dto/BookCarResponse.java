package com.carrental.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Introspected
@Serdeable
@AllArgsConstructor
public class BookCarResponse {

    private  Long bookingId;
    private BigDecimal billAmount;
    private String message;
    }


