package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.model.Person;
import com.example.demoKeycloak.service.PersonService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/createuser")
    public ResponseEntity<CustomResponse> createUser(@Valid @RequestBody Person person, Errors errors){

        CustomResponse customResponse=new CustomResponse();

        if(errors.hasErrors()){
            customResponse.setMessage("Some of the field provided by you is not valid or it is missing!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setErrorMessage(errors.toString());

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }



        customResponse=personService.createUser(person);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }

    @GetMapping("/getallusers")
    public ResponseEntity<CustomResponse> getAllUser(){
        CustomResponse customResponse=personService.getAllUsers();

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
    @GetMapping("/getuserbyid")
    public ResponseEntity<CustomResponse> getUserById(@RequestParam String id){

        CustomResponse customResponse;
        if(id==null){
            customResponse=new CustomResponse();

            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide UserId");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=personService.getUserById(id);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);


    }
    @GetMapping("/getuserbyfieldname")
    public ResponseEntity<CustomResponse> getUserByFieldName(@RequestParam(required = false) String email,@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String username){

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
    public ResponseEntity<CustomResponse> updateUser(@RequestBody Person person){

        CustomResponse customResponse=new CustomResponse();

        if(person.getId()==null){
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide id of the User!");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=personService.updateUser(person);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }


    @DeleteMapping("/deleteuser")
    public ResponseEntity<CustomResponse> deleteUser(@RequestParam String id){

        CustomResponse customResponse=new CustomResponse();

        if(id==null){
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide id of the User!");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=personService.deleteUser(id);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }


}
