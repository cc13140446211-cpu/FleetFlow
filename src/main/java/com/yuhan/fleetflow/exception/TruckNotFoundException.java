package com.yuhan.fleetflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TruckNotFoundException extends RuntimeException {

    public TruckNotFoundException(Long truckId) {
        super("Truck not found: " + truckId);
    }
}