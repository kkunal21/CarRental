package com.carrental.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Introspected
@Serdeable
public class BookCarResponse {

    private  Long bookingId;
    private BigDecimal billAmount;
    private String message;
    }


