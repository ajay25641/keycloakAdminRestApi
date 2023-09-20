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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/createuser")
    public ResponseEntity<CustomResponse> createUser(@Valid @RequestBody Person person){

        return personService
                .createUser(person)
                .responseBuilder();
    }

    @GetMapping("/getallusers")
    public ResponseEntity<CustomResponse> getAllUser(){
        return personService
                .getAllUsers()
                .responseBuilder();
    }
    @GetMapping("/getuserbyid")
    public ResponseEntity<CustomResponse> getUserById(@ValidParam @RequestParam String id){

        return personService
                .getUserById(id)
                .responseBuilder();
    }
    @GetMapping("/getuserbyfieldname")
    public ResponseEntity<CustomResponse> getUserByFieldName(
            @Email @RequestParam(required = false) String email,
            @ValidParam @RequestParam(required = false) String firstName,
            @ValidParam @RequestParam(required = false) String lastName,
            @ValidParam @RequestParam(required = false) String username){

        String fieldName;
        String fieldVal;

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
            return CustomResponse.getBuilder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Please provide the correct field Name!")
                    .responseBuilder();
        }
        return personService.getUserByFieldName(fieldVal,fieldName).responseBuilder();
    }
    @PutMapping("/updateuser")
    public ResponseEntity<CustomResponse> updateUser(@Valid @RequestBody Person person) {

        if(person.getId()==null || person.getId().length()<=3){

            return CustomResponse.getBuilder()
                    .message("userId must not be blank!")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .responseBuilder();
        }

        return personService
                .updateUser(person)
                .responseBuilder();
    }


    @DeleteMapping("/deleteuser")
    public ResponseEntity<CustomResponse> deleteUser(@ValidParam @RequestParam String id){

        return personService
                .deleteUser(id)
                .responseBuilder();
    }


}
