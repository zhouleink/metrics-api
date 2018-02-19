package com.flexreceipts.metricsapi.controllers;

import com.flexreceipts.metricsapi.exceptions.ResourceAlreadyExistsException;
import com.flexreceipts.metricsapi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Map.Entry;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final String NOT_FOUND_MSG = "Resource Not Found!";
    private final String INVALID_ID_MSG = "Invalid ID!";
    private final String ALREADY_EXISTS = "Resource with this name has already existed!";

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(NOT_FOUND_MSG,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<Object> handleNumberFormatException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(INVALID_ID_MSG,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public ResponseEntity<Object[]> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request) {
        System.out.println(Arrays.toString(exception.getParams()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Message", ALREADY_EXISTS);
        return new ResponseEntity<>(exception.getParams(), headers,
                HttpStatus.CONFLICT);
    }
}
