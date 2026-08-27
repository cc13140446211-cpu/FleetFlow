package com.yuhan.fleetflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJobStatusException extends RuntimeException {

    public InvalidJobStatusException(String status) {
        super("Invalid job status: " + status);
    }
}