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

        CustomResponse customResponse=new CustomResponse();

        String parameterName= ex.getParameterName();

        customResponse.setMessage("Parameter "+"'"+parameterName+"'"+" is "+"missing");
        customResponse.setErrorMessage(ex.getMessage());
        customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> methodArgumentNotValid(MethodArgumentNotValidException ex){
        CustomResponse customResponse=new CustomResponse();

        customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        customResponse.setErrorMessage(ex.getMessage());
        customResponse.setMessage("Some of the field provided by you is not valid or it is missing!");

        BindingResult bindingResult=ex.getBindingResult();

        List<FieldError> fieldErrorList=bindingResult.getFieldErrors();

        List<String>messages=new ArrayList<>();

        for(FieldError fieldError:fieldErrorList){
            messages.add(fieldError.getDefaultMessage());
        }

        customResponse.setErrorMessage(messages);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse> contraintViolationException(ConstraintViolationException ex){

        CustomResponse customResponse=new CustomResponse();
        customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        customResponse.setMessage("Invalid param");

        Set<ConstraintViolation<?>> violationSet=ex.getConstraintViolations();

        List<String>messages=new ArrayList<>();

        for(ConstraintViolation<?>violation:violationSet){
            String message=violation.getPropertyPath().toString()+" "+violation.getMessage();
            messages.add(message);
        }

        customResponse.setErrorMessage(messages);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> genericException(Exception e){
        CustomResponse customResponse=new CustomResponse();

        customResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        customResponse.setMessage("Unknown error has occured!");
        customResponse.setErrorMessage(e.getMessage());

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
}
