package com.example.demoKeycloak.Responses;


import lombok.Data;

@Data
public class CustomResponse {
    int statusCode;
    String message;
    String errorMessage;
    Object data;
}
