package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomResponse> handleMissingParameter(MissingServletRequestParameterException ex){
        String parameterName= ex.getParameterName();

        return CustomResponse.getBuilder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Parameter "+"'"+parameterName+"'"+" is "+"missing")
                .errorMessages(ex.getMessage())
                .responseBuilder();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> methodArgumentNotValid(MethodArgumentNotValidException ex){
        BindingResult bindingResult=ex.getBindingResult();

        List<FieldError> fieldErrorList=bindingResult.getFieldErrors();

        List<String>messages=new ArrayList<>();

        for(FieldError fieldError:fieldErrorList){
            messages.add(fieldError.getDefaultMessage());
        }

        return CustomResponse
                .getBuilder()
                .message("Some of the field provided by you is not valid or it is missing!")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessages(messages)
                .responseBuilder();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse> contraintViolationException(ConstraintViolationException ex){

        Set<ConstraintViolation<?>> violationSet=ex.getConstraintViolations();

        List<String>messages=new ArrayList<>();

        for(ConstraintViolation<?>violation:violationSet){
            String message=violation.getPropertyPath().toString()+" "+violation.getMessage();
            messages.add(message);
        }

        return CustomResponse
                .getBuilder()
                .message("Invalid param")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorMessages(messages)
                .responseBuilder();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> genericException(Exception e){
        return CustomResponse
                .getBuilder()
                .message("Unknown error has occured!")
                .errorMessages(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseBuilder();
    }
}
