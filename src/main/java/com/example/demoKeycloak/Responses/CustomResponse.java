package com.example.demoKeycloak.Responses;


import lombok.Data;

@Data
public class CustomResponse {
    int statusCode;
    String message;
    Object errorMessage;
    Object data;
}
