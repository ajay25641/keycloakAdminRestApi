package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.Validations.ValidParam;
import com.example.demoKeycloak.model.Person;
import com.example.demoKeycloak.service.PersonService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/createuser")
    public ResponseEntity<CustomResponse> createUser(@Valid @RequestBody Person person){

        CustomResponse customResponse;

        customResponse=personService.createUser(person);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }

    @GetMapping("/getallusers")
    public ResponseEntity<CustomResponse> getAllUser(){
        CustomResponse customResponse=personService.getAllUsers();

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
    @GetMapping("/getuserbyid")
    public ResponseEntity<CustomResponse> getUserById(@ValidParam @RequestParam String id){

        CustomResponse customResponse;

        customResponse=personService.getUserById(id);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);


    }
    @GetMapping("/getuserbyfieldname")
    public ResponseEntity<CustomResponse> getUserByFieldName(
            @Email @RequestParam(required = false) String email,
            @ValidParam @RequestParam(required = false) String firstName,
            @ValidParam @RequestParam(required = false) String lastName,
            @ValidParam @RequestParam(required = false) String username){

        String fieldName;
        String fieldVal;

        CustomResponse customResponse=new CustomResponse();


        if(email!=null){
            fieldVal=email;
            fieldName="email";
        }
        else if(firstName!=null){
            fieldVal=firstName;
            fieldName="firstName";
        }
        else if(lastName!=null){
            fieldVal=lastName;
            fieldName="lastName";
        }
        else if(username!=null){
            fieldVal=username;
            fieldName="username";
        }
        else{
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide the correct field Name!");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }
        customResponse=personService.getUserByFieldName(fieldVal,fieldName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
    @PutMapping("/updateuser")
    public ResponseEntity<CustomResponse> updateUser(@Valid @RequestBody Person person) throws MissingServletRequestParameterException {

        CustomResponse customResponse;

        if(person.getId()==null || person.getId().length()<=3){
            throw new MissingServletRequestParameterException("userId","String");
        }

        customResponse=personService.updateUser(person);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }


    @DeleteMapping("/deleteuser")
    public ResponseEntity<CustomResponse> deleteUser(@ValidParam @RequestParam String id){

        CustomResponse customResponse;

        customResponse=personService.deleteUser(id);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }


}
