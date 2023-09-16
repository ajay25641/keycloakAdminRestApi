package com.example.demoKeycloak.service;

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

        CustomResponse customResponse=new CustomResponse();

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
            customResponse.setMessage("Unable to create User!");
            customResponse.setStatusCode(response.getStatus());

            return customResponse;
        }

        UserRepresentation userRepresentation=keycloak.realm(realm).users().searchByEmail(person.getEmail(),true).get(0);

        customResponse.setStatusCode(HttpStatus.CREATED.value());
        customResponse.setMessage("User created successfully.");
        customResponse.setData(userRepresentation);

        return customResponse;

    }
    public CustomResponse getAllUsers() {
        CustomResponse customResponse=new CustomResponse();

        List<UserRepresentation>userRepresentationList=keycloak.realm(realm).users().list();

        if(userRepresentationList.size()==0){
            customResponse.setMessage("No User found!");
        }

        customResponse.setData(userRepresentationList);
        customResponse.setStatusCode(HttpStatus.OK.value());

        return customResponse;
    }
    public CustomResponse getUserById(String userId){

        CustomResponse customResponse=new CustomResponse();

        UserResource userResource=keycloak.realm(realm).users().get(userId);

        try{
            UserRepresentation userRepresentation=userResource.toRepresentation();

            customResponse.setStatusCode(HttpStatus.OK.value());
            customResponse.setData(userRepresentation);

            return customResponse;

        }
        catch(Exception e){
            customResponse.setMessage("User not found!");
            customResponse.setErrorMessage(e.getMessage());

            customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            return customResponse;
        }
    }
    public CustomResponse getUserByFieldName(String val,String fieldName){
        CustomResponse customResponse=new CustomResponse();

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

        if(userRepresentationList.size()==0){
            customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            customResponse.setMessage("No user found with the given "+fieldName+" !");
        }
        else{
            customResponse.setStatusCode(HttpStatus.OK.value());
        }
        customResponse.setData(userRepresentationList);
        return customResponse;

    }
    public CustomResponse deleteUser(String userId){
        CustomResponse customResponse=new CustomResponse();

        Response response=keycloak.realm(realm).users().delete(userId);
        customResponse.setStatusCode(response.getStatus());

        if(response.getStatus()==HttpStatus.NOT_FOUND.value()){
            customResponse.setMessage("User with given id does not exists!");
        }
        else if(response.getStatus()!=HttpStatus.NO_CONTENT.value()){
            customResponse.setMessage("Unable to delete User!");
        }

        return customResponse;

    }
    public CustomResponse updateUser(Person updatedPerson){
        CustomResponse customResponse=new CustomResponse();
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

            customResponse.setMessage("User successfully updated");
            customResponse.setStatusCode(HttpStatus.OK.value());
            customResponse.setData(existingPerson);

            return customResponse;


        }
        catch (Exception e){
             customResponse.setMessage("User not found with given id!");
             customResponse.setErrorMessage(e.getMessage());
             customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

             return customResponse;
        }


    }
}
