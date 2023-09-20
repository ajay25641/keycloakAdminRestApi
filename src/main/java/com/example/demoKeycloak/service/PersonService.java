package com.example.demoKeycloak.service;

import com.example.demoKeycloak.CustomException.DataNotFoundException;
import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.model.Person;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PersonService {

    @Value("${keycloak.realm}")
    private String realm;
    @Autowired
    private Keycloak keycloak;
    public CustomResponse createUser(Person person){

        UserRepresentation keyclockUser=new UserRepresentation();

        keyclockUser.setEmail(person.getEmail());
        keyclockUser.setFirstName(person.getFirstName());
        keyclockUser.setLastName(person.getLastName());
        keyclockUser.setUsername(person.getUsername());

        if(person.getPassword()!=null){
            CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(person.getPassword());
            credentialRepresentation.setTemporary(false);

            keyclockUser.setCredentials(Collections.singletonList(credentialRepresentation));

        }

        Response response=keycloak.realm(realm).users().create(keyclockUser);

        if(response.getStatus()!= HttpStatus.CREATED.value()){

            return CustomResponse.getBuilder()
                    .message("Unable to create User!")
                    .statusCode(response.getStatus())
                    .build();
        }

        UserRepresentation userRepresentation=keycloak.realm(realm).users().searchByEmail(person.getEmail(),true).get(0);

        return CustomResponse
                .getBuilder()
                .message("User created successfully.")
                .statusCode(HttpStatus.CREATED.value())
                .data(userRepresentation)
                .build();
    }
    public CustomResponse getAllUsers() {
        List<UserRepresentation>userRepresentationList=keycloak.realm(realm).users().list();

        String message=null;

        if(userRepresentationList.isEmpty()){
            message="No User found!";
        }

        return CustomResponse.getBuilder()
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .data(userRepresentationList)
                .build();
    }
    public CustomResponse getUserById(String userId){

        UserResource userResource=keycloak.realm(realm).users().get(userId);

        try{
            UserRepresentation userRepresentation=userResource.toRepresentation();

            return CustomResponse.getBuilder()
                    .statusCode(HttpStatus.OK.value())
                    .data(userRepresentation)
                    .build();
        }
        catch(Exception e){
            throw new DataNotFoundException("User",e.getMessage());
        }
    }
    public CustomResponse getUserByFieldName(String val,String fieldName){
        List<UserRepresentation> userRepresentationList;

        if(fieldName.equals("email")){
            userRepresentationList=keycloak.realm(realm).users().searchByEmail(val,true);
        }
        else if(fieldName.equals("firstName")){
            userRepresentationList=keycloak.realm(realm).users().searchByFirstName(val,true);
        }
        else if(fieldName.equals("lastName")){
            userRepresentationList=keycloak.realm(realm).users().searchByLastName(val,true);
        }
        else{
            userRepresentationList=keycloak.realm(realm).users().searchByUsername(val,true);
        }

        String message=null;
        if(userRepresentationList.isEmpty()){
            message="No user found with the given "+fieldName+"!";
        }
        return CustomResponse.getBuilder()
                .statusCode(HttpStatus.OK.value())
                .message(message).data(userRepresentationList)
                .build();
    }
    public CustomResponse deleteUser(String userId){
        Response response=keycloak.realm(realm).users().delete(userId);

        String message=null;

        if(response.getStatus()==HttpStatus.NOT_FOUND.value()){
            message="User with given id does not exists!";
        }
        else if(response.getStatus()!=HttpStatus.NO_CONTENT.value()){
            message="Unable to delete User!";
        }

        return CustomResponse.getBuilder()
                .message(message)
                .statusCode(response.getStatus())
                .build();

    }
    public CustomResponse updateUser(Person updatedPerson){

        UserResource userResource=keycloak.realm(realm).users().get(updatedPerson.getId());

        try{
            UserRepresentation existingPerson=userResource.toRepresentation();


            if(updatedPerson.getUsername()!=null){
                existingPerson.setUsername(updatedPerson.getUsername());
            }
            if(updatedPerson.getEmail()!=null){
                existingPerson.setEmail(updatedPerson.getEmail());
            }
            if(updatedPerson.getFirstName()!=null){
                existingPerson.setFirstName(updatedPerson.getFirstName());
            }
            if(updatedPerson.getLastName()!=null){
                existingPerson.setLastName(updatedPerson.getLastName());
            }
            if(updatedPerson.getEnabled()!=null){
                boolean isEnabled=updatedPerson.getEnabled().equalsIgnoreCase("true")?true:false;
                existingPerson.setEnabled(isEnabled);
            }
            if(updatedPerson.getEmailVerified()!=null){
                boolean isEmailVerified=updatedPerson.getEmailVerified().equalsIgnoreCase("true")?true:false;
                existingPerson.setEmailVerified(isEmailVerified);
            }
            userResource.update(existingPerson);

            existingPerson=keycloak.realm(realm).users().get(updatedPerson.getId()).toRepresentation();

            return CustomResponse.getBuilder()
                    .message("User successfully updated")
                    .statusCode(HttpStatus.OK.value())
                    .data(existingPerson)
                    .build();
        }
        catch (Exception e){
               throw new DataNotFoundException("User",e.getMessage());
        }
    }
}
